package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@EnableJpaRepositories
interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {

    fun findByUserId(userId: UUID?): Optional<PasswordResetToken>

    fun findByToken(token: String): Optional<PasswordResetToken>

    fun deleteByToken(token: String)

}