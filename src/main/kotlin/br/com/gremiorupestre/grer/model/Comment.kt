package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
data class Comment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column(unique = true)
    @field:NotNull
    val content : String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @field:NotNull
    val user : User = User(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @field:NotNull
    val article : Article = Article(),

    @Temporal(TemporalType.TIMESTAMP)
    var dateCreated : LocalDateTime = LocalDateTime.now()

)
