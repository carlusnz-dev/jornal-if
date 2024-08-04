package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.Comment
import br.com.gremiorupestre.grer.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var articleService: ArticleService

    @Autowired
    lateinit var userService: UserService

    fun save(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    fun delete(comment: Comment) {
        commentRepository.delete(comment)
    }

    fun findById(id: Long) = commentRepository.findById(id)

    fun findAll(): MutableList<Comment> = commentRepository.findAll()

    fun findCommentsByArticleId(articleId: Long) = commentRepository.findCommentsByArticleId(articleId)

    fun findArticleById(id: Long) = articleService.findById(id)

}