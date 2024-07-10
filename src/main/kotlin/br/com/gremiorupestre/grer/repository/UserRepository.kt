package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
@EnableJpaRepositories
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): Optional<User>

    fun findByUsername(username: String): Optional<User>

}