package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.EmailVerificationToken
import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.EmailVerificationTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailVerificationService(
    @Autowired private val tokenRepository: EmailVerificationTokenRepository,
    @Autowired private val mailSender: JavaMailSender
) {

    fun createVerificationToken(user: User): EmailVerificationToken {
        val token = UUID.randomUUID().toString()
        val emailVerificationToken = EmailVerificationToken(
            token = token,
            user = user
        )
        return tokenRepository.save(emailVerificationToken)
    }

    fun sendVerificationEmail(user: User, token: String) {
        val url = "http://localhost:8080/confirm?token=$token"

        val email = SimpleMailMessage()
        email.setTo(user.email)
        email.subject = "Confirmação de e-mail - Jornal IF"
        email.text = "Clique no link para confirmar seu e-mail: $url"
        mailSender.send(email)
    }

    fun confirmEmail(token: String) {
        val emailVerificationToken = tokenRepository.findByToken(token)
        emailVerificationToken?.let {
            it.user!!.isVerified = true
            tokenRepository.delete(it)
        }
    }

    fun deleteToken(token: String) {
        val emailVerificationToken = tokenRepository.findByToken(token)
        emailVerificationToken?.let {
            tokenRepository.delete(it)
        }
    }

    fun hasToken(user: User): Boolean {
        return tokenRepository.findByUser(user) != null
    }

}