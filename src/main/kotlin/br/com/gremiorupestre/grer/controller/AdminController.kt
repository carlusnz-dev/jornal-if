package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.service.ArticleService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminController {

    @Autowired
    lateinit var articleService: ArticleService

    @GetMapping("/admin")
    fun admin(model: Model, session: HttpSession): String {

        if (session.getAttribute("userLoged") == null) {
            return "redirect:/login?errorNotLogged"
        }

        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/?errorNotAdmin"
        }

        return "admin"
    }

}