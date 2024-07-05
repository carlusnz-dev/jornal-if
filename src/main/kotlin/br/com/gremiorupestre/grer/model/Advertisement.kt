package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
data class Advertisement(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column(unique = true)
    @field:NotNull
    val title : String = "",
    @field:NotNull
    @Lob
    val content : String = "",
    @field:NotNull
    val url : String = "",
    @field:NotNull
    val image : String = "",

    @Temporal(TemporalType.TIMESTAMP)
    var dateCreated : LocalDateTime = LocalDateTime.now(),

    @Temporal(TemporalType.TIMESTAMP)
    var dateToExpire : LocalDateTime = LocalDateTime.now(),

    )