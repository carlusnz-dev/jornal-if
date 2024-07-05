package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {
}