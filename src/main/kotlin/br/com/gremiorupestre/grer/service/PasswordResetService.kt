package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.PasswordResetToken
import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.PasswordResetTokenRepository
import br.com.gremiorupestre.grer.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
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

    fun createPasswordResetTokenForUser(email: String): String {
        val optionalUser: Optional<User> = userRepository.findByEmail(email)
        val user = optionalUser.orElseThrow { NoSuchElementException("Usuário não encontrado para o email: $email") }

        val token = UUID.randomUUID().toString()
        val expiryDate = LocalDateTime.now().plusMinutes(30)

        val passwordResetToken = PasswordResetToken(
            token = token,
            user = user,
            expiryDate = expiryDate
        )
        passwordResetTokenRepository.save(passwordResetToken)

        // Send email with token
        mailService.sendEmail(
            to = user.email,
            subject = "Reset Password",
            text = "Para redefinir sua senha, clique no link abaixo:\n\n" +
                    "https://jornal.gremiorupestre.com.br/reset-password/$token"
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
        val passwordResetToken = passwordResetTokenRepository.findByToken(token)

        if (passwordResetToken.isPresent && passwordResetToken.get().isValid()) {
            return "valid"
        }

        return "invalid"
    }

    fun deletePasswordResetToken(token: String) {
        passwordResetTokenRepository.deleteByToken(token)
    }
}
