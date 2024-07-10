package br.com.gremiorupestre.grer.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailConfig {

    @Value("\${spring.mail.host}")
    private lateinit var host: String

    @Value("\${spring.mail.port}")
    private lateinit var port: String

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    @Value("\${spring.mail.password}")
    private lateinit var password: String

    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = host
        mailSender.port = port.toInt()
        mailSender.username = username
        mailSender.password = password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }

}