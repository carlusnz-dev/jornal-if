package br.com.gremiorupestre.grer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : UUID? = UUID.randomUUID(),

    @Column(unique = true)
    @field:NotNull
    val username : String = "",
    @field:NotNull
    val name : String = "",
    @field:NotNull
    val surname : String = "",

    @Column(unique = true)
    @field:NotNull
    val email : String = "",
    @JsonIgnore
    var password : String = "",

    @Column(nullable = false)
    var isVerified: Boolean = false,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinTable(name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: List<Role> = mutableListOf(),

    @Temporal(TemporalType.TIMESTAMP)
    var dateCreated : LocalDateTime = LocalDateTime.now(),

    @field:NotNull
    var imageUrl : String = ""

) : Serializable