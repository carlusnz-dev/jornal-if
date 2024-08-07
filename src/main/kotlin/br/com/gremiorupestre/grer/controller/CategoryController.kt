package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/category")
class CategoryController {

    @Autowired
    lateinit var categoryService: CategoryService

    @GetMapping()
    fun listCategories(model: Model): String {
        model.addAttribute("categories", categoryService.findAll())
        return "categories/list"
    }

}