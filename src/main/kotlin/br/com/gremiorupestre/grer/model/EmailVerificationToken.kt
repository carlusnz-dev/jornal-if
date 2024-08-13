package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
data class EmailVerificationToken(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false)
    @field:NotNull
    val token: String = "",

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "user_id")
    val user: User?,

    val expiryDate: LocalDateTime = LocalDateTime.now().plusDays(1) // Expira em 1 dia

) {
    fun isValid(): Boolean {
        return expiryDate.isAfter(LocalDateTime.now())
    }
}
