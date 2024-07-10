package br.com.gremiorupestre.grer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailService {

    @Autowired
    private lateinit var mailSender: JavaMailSender

    fun sendEmail(
        to: String,
        subject: String,
        text: String
    ) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text)

        mailSender.send(message)

        println("Email sent successfully.") ?: println("Failed to send email.")
    }

}