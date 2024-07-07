package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.service.ArticleService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @Autowired
    private lateinit var articleService: ArticleService

    @GetMapping("/")
    fun home(model: Model, httpServletRequest: HttpServletRequest): String {

        // Session
        val session = httpServletRequest.session

        if (session.getAttribute("userLoged") == null) {
            return "redirect:/login?errorNotLogged"
        }

        // Articles
        val articles = articleService.findAll()
        model.addAttribute("articles", articles)

        // User
        val user = session.getAttribute("userLoged") as User

        if (user.roles.contains("ADMIN")) {
            model.addAttribute("isAdmin", true)
        }

        model.addAttribute("user", user)

        return "home"
    }

}