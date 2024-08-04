package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.service.ArticleService
import br.com.gremiorupestre.grer.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @Autowired
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/")
    fun home(model: Model): String {

        val auth = SecurityContextHolder.getContext().authentication
        if (auth.principal is User) {
            val user = auth.principal as User
            model.addAttribute("user", userService.findById(user.id!!)?.get())
        }

        val articles = articleService.findAllByOrderByDateCreatedDesc()
        model.addAttribute("articles", articles)

        return "home"
    }

}