package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.Article
import br.com.gremiorupestre.grer.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ArticleService {

    @Autowired
    lateinit var articleRepository: ArticleRepository

    fun findAll(): List<Article> {
        return articleRepository.findAll()
    }

    fun findById(id: Long): Optional<Article> {
        return articleRepository.findById(id)
    }

    fun save(article: Article): Article {
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
                tags = updatedArticle.tags,
                comments = updatedArticle.comments
            )
            articleRepository.save(articleToSave)
        }
    }
}
