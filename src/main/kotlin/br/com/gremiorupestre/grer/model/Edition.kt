package br.com.gremiorupestre.grer.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import org.jetbrains.annotations.NotNull

@Entity
data class Edition(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long? = null,

    @Column(unique = true)
    @field:NotNull
    val name : String = "",

    @ManyToMany(mappedBy = "editions")
    val articles : MutableSet<Article> = mutableSetOf()

)
