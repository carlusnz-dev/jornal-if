package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.PasswordResetTokenRepository
import br.com.gremiorupestre.grer.repository.RoleRepository
import br.com.gremiorupestre.grer.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService (
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordResetService: PasswordResetService
) {

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    fun findByUsername(username: String) = userRepository.findByUsername(username)

    fun findByEmail(email: String) = userRepository.findByEmail(email)

    fun findById(id: UUID) = userRepository.findById(id)

    fun saveUser(user: User) {

        user.password = bCryptPasswordEncoder.encode(user.password)
        val roleUser = roleRepository.findById(2L).orElse(null)
        user.roles = mutableListOf(roleUser)
        userRepository.save(user)

    }

    // Reset password
    fun resetPassword(username: String) {
        passwordResetService.createPasswordResetTokenForUser(username)
    }

    // Update password
    fun updatePassword(token: String, password: String) {
        val passwordResetToken = passwordResetService.findByToken(token)
        val user = passwordResetToken.get().user
        user.password = bCryptPasswordEncoder.encode(password)
        userRepository.save(user)
    }

    // Validate password reset token
    fun validatePasswordResetToken(token: String) = passwordResetService.validatePasswordResetToken(token)

    // Send email

}