package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.RoleRepository
import br.com.gremiorupestre.grer.repository.UserRepository
import br.com.gremiorupestre.grer.security.SecurityConfig
import br.com.gremiorupestre.grer.service.UserService
import br.com.gremiorupestre.grer.util.FileUtil
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class LogUserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var fileUtil: FileUtil

    // Get Mapping
    @GetMapping("/login")
    fun returnLoginPage(): String {
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
        @RequestParam("image") image: MultipartFile,
    ): String {

        // Check e-mail
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")
        if (!emailPattern.matches(user.email)) {
            model.addAttribute("error", "Invalid email")
            return "redirect:/register?errorEmail"
        }

        val imagePath = fileUtil.saveFile(image)
        if (imagePath.isEmpty()) {
            model.addAttribute("error", "Failed to save image")
            return "redirect:/register?errorImage"
        }

        user.imageUrl = "https://firebasestorage.googleapis.com/v0/b/jornal-if-61544.appspot.com/o/$imagePath?alt=media"

        userService.saveUser(user)

        if (user.id != null) {
            println("User saved with success: ${user.id}")
        }

        return "redirect:/login"
    }

}