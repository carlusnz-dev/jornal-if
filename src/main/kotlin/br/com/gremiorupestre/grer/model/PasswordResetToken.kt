package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import java.util.*

@Entity
data class PasswordResetToken(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false)
    @field:NotNull
    val token: String = "",

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @field:NotNull
    val user: User,

    @Temporal(TemporalType.TIMESTAMP)
    val expiryDate: LocalDateTime

) {
    fun isValid(): Boolean {
        return expiryDate.isAfter(LocalDateTime.now())
    }
}
