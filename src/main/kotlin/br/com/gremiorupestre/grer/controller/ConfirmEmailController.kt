package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.security.userdetails.UserDetailsImpl
import br.com.gremiorupestre.grer.service.EmailVerificationService
import br.com.gremiorupestre.grer.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ConfirmEmailController {

    @Autowired
    lateinit var emailService: EmailVerificationService

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/confirm")
    fun confirmEmail(@RequestParam("token") token: String): String {
        emailService.confirmEmail(token)
        // delete token
        emailService.deleteToken(token)
        return "redirect:/login?emailConfirmed"
    }

    @GetMapping("/create-token")
    fun createTokenEmail(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal is UserDetailsImpl) {
            val userDetail = authentication.principal as UserDetailsImpl
            val user = userService.findById(userDetail.getId()!!)?.get()
            if (user != null) {
                if (user.isVerified) {
                    return "redirect:/profile?emailAlreadyVerified"
                } else if (emailService.hasToken(user)) {
                    return "redirect:/profile?emailAlreadySent"
                }
                val token = emailService.createVerificationToken(user)
                emailService.sendVerificationEmail(user, token.token)
            }
        } else {
            return "redirect:/login"
        }

        return "redirect:/profile?verificationEmailSent"
    }

    @GetMapping("/resend-verification-email")
    fun resendVerificationEmail(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal is UserDetailsImpl) {
            val userDetail = authentication.principal as UserDetailsImpl
            val user = userService.findById(userDetail.getId()!!)?.get()
            if (user != null) {
                if (user.isVerified) {
                    return "redirect:/profile?emailAlreadyVerified"
                } else if (emailService.hasToken(user)) {
                    return "redirect:/profile?emailAlreadySent"
                }
                val token = emailService.createVerificationToken(user)
                emailService.sendVerificationEmail(user, token.token)
            }
        } else {
            return "redirect:/login"
        }

        return "redirect:/profile?verificationEmailSent"
    }

}