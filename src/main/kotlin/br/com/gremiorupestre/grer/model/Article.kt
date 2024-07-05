package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
data class Article(

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
    val imageUrl : String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @field:NotNull
    val user : User = User(),

    @Temporal(TemporalType.TIMESTAMP)
    var dateCreated : LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @field:NotNull
    val category : Category = Category(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "article_tags",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags : MutableSet<Tag> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "article")
    val comments : MutableSet<Comment> = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "article_edition",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "edition_id")]
    )
    val editions : MutableSet<Edition> = mutableSetOf()


)
