package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.UserRepository
import br.com.gremiorupestre.grer.security.SecurityConfig
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Suppress("UNREACHABLE_CODE")
@Controller
class LogUserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Lazy
    @Autowired
    private lateinit var securityConfig: SecurityConfig

    // Get Mapping
    @GetMapping("/login")
    fun returnLoginPage(
        model: Model,
        request: HttpServletRequest
    ): String {

        val session = request.session

        if (session.getAttribute("userLoged") != null) {
            return "redirect:/"
        }

        val user = User()
        model.addAttribute("user", user)

        return "login"
    }

    @GetMapping("/register")
    fun returnRegisterPage(model: Model): String {

        model.addAttribute("user", User())

        return "register"
    }

    // Post Mapping

    @Transactional
    @PostMapping("/register/user")
    fun registerUser(
        model: Model,
        user: User,
    ): String {

        val userExists = userRepository.findByUsername(user.username)
        val emailExists = userRepository.findByEmail(user.email)

        // Check e-mail
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")
        if (!emailPattern.matches(user.email)) {
            model.addAttribute("error", "Invalid email")
            return "redirect:/register?errorEmail"
        }

        if (userExists != null) {
            model.addAttribute("errorUser", "Username already exists")
            return "redirect:/register?errorUser"
        } else if (emailExists != null) {
            model.addAttribute("errorUser", "Email already exists")
            return "redirect:/register?errorUser"
        }

        user.password = securityConfig.passwordEncoder().encode(user.password)
        user.roles.add("USER")

        userRepository.save(user)
        println("User registered successfully!") ?: "User not registered"

        return "redirect:/login"
    }

    @Transactional
    @PostMapping("/login/user")
    fun loginUser(
        user: User,
        request: HttpServletRequest
    ): String {

        val session = request.session

        val userBD = userRepository.findByUsername(user.username) ?: return "redirect:/login?error"

        if (securityConfig.passwordEncoder().matches(user.password, userBD.password)) {
            val auth = UsernamePasswordAuthenticationToken(userBD, null, emptyList())
            SecurityContextHolder.getContext().authentication = auth
            session.setAttribute("userLoged", userBD)

            return "redirect:/"
        } else {
            return "redirect:/login?error"
        }

        println(userBD) ?: "User not found"

        return "redirect:/"
    }

    @PostMapping("/login/guest")
    fun loginGuest(
        request: HttpServletRequest
    ): String {

        val session = request.session

        val user = User(
            username = "guest",
            name = "Guest",
            surname = "",
            email = "",
            password = "",
            roles = mutableSetOf("GUEST")
        )

        session.setAttribute("userLoged", user)

        return "redirect:/"
    }

    // Logout
    @GetMapping("/logout")
    fun logoutUser(
        request: HttpServletRequest
    ): String {

        val session = request.session
        SecurityContextHolder.clearContext()
        session.invalidate()
        println("User logged out successfully!")

        return "redirect:/login"
    }

}