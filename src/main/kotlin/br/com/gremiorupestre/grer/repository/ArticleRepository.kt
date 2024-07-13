package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.Article
import br.com.gremiorupestre.grer.model.Edition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface ArticleRepository : JpaRepository<Article, Long> {

    fun findByEdition(edition: Edition): List<Article>

    fun findAllByEdition(edition: Edition): List<Article>

    fun findByTitleContainingIgnoreCase(title: String): List<Article>

    fun findAllByOrderByDateCreatedDesc(): List<Article>

}