package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.Article
import br.com.gremiorupestre.grer.model.Edition
import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.model.View
import br.com.gremiorupestre.grer.repository.ArticleRepository
import br.com.gremiorupestre.grer.repository.ViewRepository
import br.com.gremiorupestre.grer.security.userdetails.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class ArticleService {

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var vi: ViewRepository

    @Autowired
    lateinit var userService: UserService

    fun findAll(): List<Article> {
        return articleRepository.findAll()
    }

    fun findAllByEditionsAndId(
        editions: Edition,
        id: Long
    ): List<Article> {
        return articleRepository.findAllByEditionAndId(editions, id)
    }

    fun findAllByEditions(editions: Edition): List<Article> {
        return articleRepository.findAllByEdition(editions)
    }

    fun findById(id: Long): Optional<Article> {
        return articleRepository.findById(id)
    }

    fun findAllByOrderByDateCreatedDesc(): List<Article> {
        return articleRepository.findAllByOrderByDateCreatedDesc()
    }

    fun save(
        article: Article
    ): Article {

        val authentication = SecurityContextHolder.getContext().authentication
        val userDetail = authentication.principal as UserDetailsImpl
        val user = userService.findById(userDetail.getId()!!)
            ?: throw Exception("User not found with ID: ${userDetail.getId()}")

        article.user = user.get()
        article.author = user.get().name

        return articleRepository.save(article)
    }

    fun deleteById(id: Long) {
        articleRepository.deleteById(id)
    }

    fun updateArticle(id: Long, updatedArticle: Article): Optional<Article> {
        return articleRepository.findById(id).map { existingArticle ->
            val articleToSave = existingArticle.copy(
                title = updatedArticle.title,
                content = updatedArticle.content,
                user = updatedArticle.user,
                category = updatedArticle.category,
                edition = updatedArticle.edition,
                tags = updatedArticle.tags,
                comments = updatedArticle.comments
            )
            articleRepository.save(articleToSave)
        }
    }

    fun trackView(article: Article, user: User) {
        val articleView = View(article = article, user = user)
        vi.save(articleView)
    }

    fun searchByTitle(title: String): List<Article> {
        return articleRepository.findByTitleContainingIgnoreCase(title)
    }

}
