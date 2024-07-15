package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import java.util.*

@Entity
data class View(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    @field:NotNull
    val user : User = User(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "article_id")
    @field:NotNull
    val article : Article = Article(),

    @Temporal(TemporalType.TIMESTAMP)
    var dateViewed : LocalDateTime = LocalDateTime.now()

) {
    override fun hashCode(): Int {
        return Objects.hash(id, article.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is View) return false
        return id == other.id
    }
}
