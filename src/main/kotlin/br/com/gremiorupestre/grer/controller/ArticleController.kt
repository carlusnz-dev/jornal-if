package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.Article
import br.com.gremiorupestre.grer.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/articles")
class ArticleController {

    @Autowired
    lateinit var articleService: ArticleService

    @GetMapping
    fun listArticles(model: Model): String {
        model.addAttribute("articles", articleService.findAll())
        return "articles/list"
    }

    @GetMapping("/{id}")
    fun getArticleById(@PathVariable id: Long, model: Model): String {
        val article = articleService.findById(id)
        if (article.isPresent) {
            model.addAttribute("article", article.get())
            return "articles/detail"
        } else {
            return "redirect:/articles?error=notfound"
        }
    }

    @GetMapping("/new")
    fun showNewArticleForm(model: Model): String {
        model.addAttribute("article", Article())
        return "articles/new"
    }

    @PostMapping
    fun createArticle(@ModelAttribute article: Article): String {
        articleService.save(article)
        return "redirect:/articles"
    }

    @GetMapping("/edit/{id}")
    fun showEditArticleForm(@PathVariable id: Long, model: Model): String {
        val article = articleService.findById(id)
        if (article.isPresent) {
            model.addAttribute("article", article.get())
            return "articles/edit"
        } else {
            return "redirect:/articles?error=notfound"
        }
    }

    @PostMapping("/update/{id}")
    fun updateArticle(@PathVariable id: Long, @ModelAttribute updatedArticle: Article): String {
        articleService.updateArticle(id, updatedArticle)
        return "redirect:/articles"
    }

    @GetMapping("/delete/{id}")
    fun deleteArticle(@PathVariable id: Long): String {
        articleService.deleteById(id)
        return "redirect:/articles"
    }
}
