package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
data class Comment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column(unique = true, nullable = false, length = 500)
    @field:NotNull
    var content : String = "",

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    @field:NotNull
    var user : User? = User(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "article_id")
    @field:NotNull
    var article : Article = Article(),

    @Temporal(TemporalType.TIMESTAMP)
    var dateCreated : LocalDateTime = LocalDateTime.now()

)
