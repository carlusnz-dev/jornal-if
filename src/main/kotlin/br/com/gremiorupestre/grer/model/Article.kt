package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.springframework.data.repository.query.Param
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
    val subtitle : String = "",
    @field:NotNull
    @Lob
    @Column(length = 6454)
    val content : String = "",
    @field:NotNull
    var imageUrl : String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @field:NotNull
    var user : User = User(),

    @field:NotNull
    var author : String = "",

    @Temporal(TemporalType.TIMESTAMP)
    var dateCreated : LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "category_id",
        referencedColumnName = "id"
    )
    @field:NotNull
    var category : Category = Category(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "article_tags",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags : MutableSet<Tag> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "article")
    val comments : MutableSet<Comment> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "edition_id",
        referencedColumnName = "id"
    )
    @field:NotNull
    var edition : Edition,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "article_view",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val views : MutableSet<User> = mutableSetOf()

)
