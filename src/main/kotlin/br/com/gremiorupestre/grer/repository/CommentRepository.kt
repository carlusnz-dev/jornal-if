package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findCommentsByArticleId(articleId: Long): MutableList<Comment>
}