package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.EmailVerificationTokenRepository

import br.com.gremiorupestre.grer.service.EmailVerificationService
import br.com.gremiorupestre.grer.service.UserService
import br.com.gremiorupestre.grer.util.FileUtil
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
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
    private lateinit var fileUtil: FileUtil

    @Autowired
    private lateinit var emailVerificationService: EmailVerificationService

    @Autowired
    private lateinit var tokenRepository: EmailVerificationTokenRepository

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
        // Validação do e-mail
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")
        if (!emailPattern.matches(user.email)) {
            model.addAttribute("error", "Invalid email")
            return "redirect:/register?errorEmail"
        }

        // Salvamento da imagem
        val imagePath = fileUtil.saveFile(image)
        if (imagePath.isEmpty()) {
            model.addAttribute("error", "Failed to save image")
            return "redirect:/register?errorImage"
        }

        user.imageUrl = "https://firebasestorage.googleapis.com/v0/b/jornal-if-61544.appspot.com/o/$imagePath?alt=media"

        userService.saveUser(user)

        return "redirect:/login"
    }

//    @GetMapping("/verify")
//    fun verifyEmail(@RequestParam("token") token: String, model: Model): String {
//        val emailVerificationToken = tokenRepository.findByToken(token)
//
//        if (emailVerificationToken == null || !emailVerificationToken.isValid()) {
//            model.addAttribute("error", "Invalid or expired token")
//            return "redirect:/login?error"
//        }
//
//        val user = emailVerificationToken.user
//        user?.isVerified = true
//        userService.saveUser(user!!)
//
//        return "redirect:/login?verified"
//    }
}
