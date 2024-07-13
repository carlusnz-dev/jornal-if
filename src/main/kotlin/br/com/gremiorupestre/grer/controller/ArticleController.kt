package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.Article
import br.com.gremiorupestre.grer.security.userdetails.UserDetailsImpl
import br.com.gremiorupestre.grer.service.ArticleService
import br.com.gremiorupestre.grer.service.CategoryService
import br.com.gremiorupestre.grer.service.EditionService
import br.com.gremiorupestre.grer.service.UserService
import br.com.gremiorupestre.grer.util.FileUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/articles")
class ArticleController {

    @Autowired
    lateinit var articleService: ArticleService

    @Autowired
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var editionService: EditionService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var fileUtil: FileUtil

    @GetMapping
    fun listArticles(model: Model): String {
        model.addAttribute("articles", articleService.findAll())
        return "articles/list"
    }

    @GetMapping(value = ["/{id}"])
    fun getArticleById(@PathVariable id: Long, model: Model): String {
        val article = articleService.findById(id)
        if (article.isPresent) {

            // Register view
            val articleOptional = article.get()
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication.principal is UserDetailsImpl) {
                val userDetail = authentication.principal as UserDetailsImpl
                val user = userService.findById(userDetail.getId()!!).get()
                articleService.trackView(articleOptional, user)
            }

            // Count views
            val views = articleOptional.views.size
            model.addAttribute("views", views)

            // Share URLs
            val getUrl = "https://jornal.gremiorupestre.com.br/articles/${article.get().id}"
            val shareUrlWhatsApp = "https://wa.me/?text=Jornal%20IF%20Gremio%20Rupestre%20-%20${article.get().title}%20${getUrl}"
            val shareUrlFacebook = "https://www.facebook.com/sharer/sharer.php?u=${getUrl}"
            val shareUrlTwitter = "https://twitter.com/intent/tweet?url=${getUrl}&text=Jornal%20IF%20Gremio%20Rupestre%20-%20Confira%20a%20matéria%20${article.get().title}"
            model.addAttribute("shareUrlWhatsApp", shareUrlWhatsApp)
            model.addAttribute("shareUrlFacebook", shareUrlFacebook)
            model.addAttribute("shareUrlTwitter", shareUrlTwitter)

            // Article
            model.addAttribute("article", article.get())

            return "articles/detail"
        } else {
            return "redirect:/articles?error=notfound"
        }
    }

    @GetMapping("/new")
    fun showNewArticleForm(model: Model): String {
        model.addAttribute("article", Article())
        model.addAttribute("categories", categoryService.findAll())
        model.addAttribute("editions", editionService.findAll())
        return "articles/article-new"
    }

    @PostMapping("/new-article")
    fun createArticle(
        @ModelAttribute article: Article,
        @RequestParam("image") image: MultipartFile
        ): String {

        val authentication = SecurityContextHolder.getContext().authentication
        val userDetail = authentication.principal as UserDetailsImpl
        val user = userService.findById(userDetail.getId()!!).get()
        article.user = user

        val edition = article.edition.id?.let { editionService.findById(it).orElseThrow { Exception("Edição não encontrada") } }
        if (edition != null) {
            article.edition = edition
        }

        val imagePath = fileUtil.saveFile(image)

        if (imagePath.isEmpty()) {
            throw IllegalArgumentException("Falha ao salvar a imagem. O caminho da imagem está vazio.")
        }

        article.imageUrl = "https://firebasestorage.googleapis.com/v0/b/jornal-if-61544.appspot.com/o/$imagePath?alt=media"
        article.author = article.user.name
        articleService.save(article)
        return "redirect:/articles"
    }

    @GetMapping("/edit/{id}")
    fun showEditArticleForm(@PathVariable id: Long, model: Model): String {
        val article = articleService.findById(id)
        if (article.isPresent) {
            model.addAttribute("article", article.get())
            model.addAttribute("categories", categoryService.findAll())
            model.addAttribute("editions", editionService.findAll())
            return "articles/edit"
        } else {
            return "redirect:/articles?error=notfound"
        }
    }

    @PostMapping("/update/{id}")
    fun updateArticle(
        @PathVariable id: Long,
        @ModelAttribute updatedArticle: Article,
        @RequestParam("category") categoryId: Long,
    ): String {

        val category = categoryService.findById(categoryId).orElseThrow { Exception("Categoria não encontrada") }
        updatedArticle.category = category

        val authentication = SecurityContextHolder.getContext().authentication
        val userDetail = authentication.principal as UserDetailsImpl
        val user = userService.findById(userDetail.getId()!!).get()
        updatedArticle.user = user

        val edition = updatedArticle.edition.id?.let { editionService.findById(it).orElseThrow { Exception("Edição não encontrada") } }
        if (edition != null) {
            updatedArticle.edition = edition
        }

        if (updatedArticle.author.isEmpty()) {
            updatedArticle.author = user.name
        }

        articleService.updateArticle(id, updatedArticle)
        return "redirect:/articles"
    }

    @GetMapping("/delete/{id}")
    fun deleteArticle(@PathVariable id: Long): String {
        articleService.deleteById(id)
        return "redirect:/articles"
    }

    @GetMapping("/search")
    fun searchArticle(@RequestParam("q") query: String?, model: Model): String {
        if (!query.isNullOrBlank()) {
            model.addAttribute("articles", articleService.searchByTitle(query))
        }

        if (query.isNullOrBlank()) {
            model.addAttribute("errorList", "Digite algo para pesquisar")
            return "articles/list?errorList"
        }

        return "articles/list"
    }

}
