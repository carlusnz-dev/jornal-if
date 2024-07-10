package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.service.PasswordResetService
import br.com.gremiorupestre.grer.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class ResetPassController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var resetService: PasswordResetService

    // GET Mapping
    @GetMapping("/reset-password/{token}")
    fun returnResetPassPage(
        @PathVariable("token") token: String,
        model: Model
    ): String {
        val getEmail = SecurityContextHolder.getContext().authentication.name
        val user = userService.findByEmail(getEmail)
        model.addAttribute("user", user)

        val resetToken = resetService.findByToken(token)

        if (resetToken.isPresent && resetToken.get().isValid()) {
            model.addAttribute("token", token)
            return "reset-password"
        }

        model.addAttribute("error", "Token inválido ou expirado.")
        return "reset-password"
    }

    // POST Mapping
    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestParam("token") token: String,
        @RequestParam("password") newPassword: String,
        @RequestParam("confirmPassword") confirmPassword: String,
        model: Model
    ): String {
        val passwordResetToken = resetService.findByToken(token)

        if (passwordResetToken.isPresent && passwordResetToken.get().isValid()) {
            val user = passwordResetToken.get().user

            if (newPassword == confirmPassword) {
                user.password = newPassword
                userService.saveUser(user)
                return "redirect:/login"
            } else {
                model.addAttribute("error", "As senhas não coincidem.")
            }
        } else {
            model.addAttribute("error", "Token inválido ou expirado.")
        }

        return "reset-password"
    }

    // GET Mapping
    @GetMapping("/forgot-password")
    fun returnForgotPassPage(): String {
        return "forgot-password"
    }

    // POST Mapping
    @PostMapping("/forgot-password")
    fun forgotPassword(
        @RequestParam("email") email: String,
        model: Model
    ): String {
        val user = userService.findByEmail(email)

        if (user.isPresent) {
            val token = resetService.createPasswordResetTokenForUser(email)
            return "redirect:/reset-password/$token"
        } else {
            model.addAttribute("error", "Usuário não encontrado.")
            return "redirect:/forgot-password?error"
        }
    }
}
