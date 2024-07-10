package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.repository.RoleRepository
import br.com.gremiorupestre.grer.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService (
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
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

}