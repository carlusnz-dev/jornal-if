package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.PasswordResetToken
import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.PasswordResetTokenRepository
import br.com.gremiorupestre.grer.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime
import java.util.*

@Service
class PasswordResetService {

    @Autowired
    private lateinit var passwordResetTokenRepository: PasswordResetTokenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    @Lazy
    private lateinit var mailService: MailService

    fun createPasswordResetTokenForUser(
        @RequestParam("email") email: String
    ) : String {

        val optionalUser : Optional<User> = userRepository.findByEmail(email)
        val user = optionalUser.get()
        println("User: $user")

        val token = UUID.randomUUID().toString()
        val expiryDate = LocalDateTime.now().plusMinutes(30)

        val myToken = PasswordResetToken(
            token = token,
            user = user,
            expiryDate = expiryDate
        )
        passwordResetTokenRepository.save(myToken)

        // Send email with token
        mailService.sendEmail(
            to = user.email,
            subject = "Reset Password",
            text = "To reset your password, click the link below:\n\n" +
                "http://localhost:8080/reset-password=${token}"
        )

        return token
    }

    fun findByUserId(userId: UUID?): Optional<PasswordResetToken> {
        return passwordResetTokenRepository.findByUserId(userId)
    }

    fun findByToken(token: String): Optional<PasswordResetToken> {
        return passwordResetTokenRepository.findByToken(token)
    }

    fun validatePasswordResetToken(token: String): String {

        val userID = userRepository.findByEmail(token).get().id
        val passwordResetToken = passwordResetTokenRepository.findByUserId(userID)

        if (passwordResetToken.isPresent) {
            val resetToken = passwordResetToken.get()
            val user = resetToken.user

            if (resetToken.expiryDate.isBefore(LocalDateTime.now())) {
                return "expired"
            }

            return "valid"
        }

        return "invalid"
    }

}