package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.EmailVerificationToken
import br.com.gremiorupestre.grer.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailVerificationTokenRepository : JpaRepository<EmailVerificationToken, Long> {
    fun findByToken(token: String): EmailVerificationToken?
    fun findByUser(user: User): EmailVerificationToken?
}