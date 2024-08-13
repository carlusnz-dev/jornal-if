package br.com.gremiorupestre.grer.security.userdetails

import br.com.gremiorupestre.grer.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow {
            RuntimeException("Usuário não encontrado")
        }

        return UserDetailsImpl(user)
    }

    fun loadUserById(id: UUID): UserDetails {
        val user = userRepository.findById(id).orElseThrow {
            RuntimeException("Usuário não encontrado")
        }
        return UserDetailsImpl(user)
    }
}