package br.com.gremiorupestre.grer.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
data class Edition(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long? = null,

    @Column(unique = true)
    @field:NotNull
    val name : String = "",
    @field:NotNull
    val number : Int = 0,
    @field:NotNull
    val description : String = "",
    @field:NotNull
    var imageUrl : String = "",

    @Temporal(TemporalType.TIMESTAMP)
    var date : LocalDateTime = LocalDateTime.now(),

    @OneToMany(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY, mappedBy = "edition")
    @JoinColumn(
        name = "edition_id",
        referencedColumnName = "id"
    )
    @field:NotNull
    val articles : MutableSet<Article> = mutableSetOf()

)
