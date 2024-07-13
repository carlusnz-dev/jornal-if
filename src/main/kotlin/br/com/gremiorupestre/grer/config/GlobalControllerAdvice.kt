package br.com.gremiorupestre.grer.config

import br.com.gremiorupestre.grer.service.CategoryService
import br.com.gremiorupestre.grer.service.EditionService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class GlobalControllerAdvice {

    @Autowired
    lateinit var editionService: EditionService

    @Autowired
    lateinit var categoryService: CategoryService

    @ModelAttribute("editions")
    fun editions(model: Model) = editionService.findAll()

    @ModelAttribute("categories")
    fun categories(model: Model) = categoryService.findAll()

    @ModelAttribute("currentUrl")
    fun currentUrl(model: Model, request: HttpServletRequest): String = request.requestURI

}