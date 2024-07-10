package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.service.ArticleService
import jakarta.servlet.http.HttpServletRequest
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
    fun admin(request: HttpServletRequest): String {

        // get user role
        val userName = request.userPrincipal.name
        val user = request.session.getAttribute(userName)

        return "admin"
    }

}