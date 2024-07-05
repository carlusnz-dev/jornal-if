package br.com.gremiorupestre.grer.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.jetbrains.annotations.NotNull

@Entity
data class Tag(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long? = null,

    @field:NotNull
    val name : String = ""

)
