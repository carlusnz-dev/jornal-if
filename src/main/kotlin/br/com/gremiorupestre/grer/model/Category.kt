package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Lazy

@Entity
data class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column(unique = true)
    @field:NotNull
    val name : String = "",
    @field:NotNull
    val description : String = "",

)
