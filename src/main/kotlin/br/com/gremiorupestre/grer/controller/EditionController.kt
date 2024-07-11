package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.Edition
import br.com.gremiorupestre.grer.service.ArticleService
import br.com.gremiorupestre.grer.service.EditionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
@RequestMapping("/editions")
class EditionController {

    @Autowired
    lateinit var editionService: EditionService

    @Autowired
    lateinit var articleService: ArticleService

    @GetMapping
    fun listEditions(model: Model) : String {
        model.addAttribute("editions", editionService.findAll())
        return "edition/list"
    }

    @GetMapping("/{id}")
    fun getEditionById(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val editionOptional = editionService.findById(id)
        if (editionOptional.isPresent) {
            val edition = editionOptional.get()

            // Passa o conjunto de edições para o serviço de artigos
            model.addAttribute("edition", edition)
            model.addAttribute("articles", articleService.findAllByEditionsAndId(edition, id))
            return "edition/view"
        }
        return "redirect:/editions"
    }

    @GetMapping(value = ["/{id}/edit"])
    fun editEdition(
        @PathVariable id: Long,
        model: Model,
    ) : String {
        val edition = editionService.findById(id)
        if (edition.isPresent) {
            model.addAttribute("edition", edition.get())
            return "edition/edit"
        }
        return "redirect:/edition"
    }

    @GetMapping(value = ["/new"])
    fun newEdition(model: Model) : String {
        model.addAttribute("edition", Edition())
        return "edition/new"
    }

    @GetMapping(value = ["/{id}/delete"])
    fun deleteEdition(
        @PathVariable id: Long,
        model: Model,
    ) : String {
        editionService.deleteById(id)
        return "redirect:/editions"
    }

    @GetMapping(value = ["/{id}/articles"])
    fun listArticles(
        @PathVariable id: Long,
        model: Model,
    ) : String {
        val edition = editionService.findById(id)
        if (edition.isPresent) {
            val editions = edition.get()
            model.addAttribute("edition", edition.get())
            model.addAttribute("articles", articleService.findAllByEditions(editions))
            return "edition/articles"
        }
        return "redirect:/editions"
    }

    @PostMapping("/save")
    fun saveEdition(edition: Edition): String {
        editionService.save(edition)
        return "redirect:/editions"
    }

    @PostMapping("/update/{id}")
    fun updateEdition(
        @PathVariable id: Long,
        edition: Edition,
    ): String {

        val localDate = LocalDateTime.now()

        edition.date = localDate

        editionService.updateEdition(id, edition)
        return "redirect:/editions"
    }

}