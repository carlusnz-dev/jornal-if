package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProfileController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/profile")
    fun returnProfilePage(
        model: Model,
        session: HttpSession
    ): String {

        if (session.getAttribute("userLoged") == null) {
            return "redirect:/login?errorNotLogged"
        }

        val user = session.getAttribute("userLoged") as User

        model.addAttribute("user", user)

        return "profile"
    }

}