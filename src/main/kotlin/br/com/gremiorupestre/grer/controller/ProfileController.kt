package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.User
import br.com.gremiorupestre.grer.security.userdetails.UserDetailsImpl
import br.com.gremiorupestre.grer.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/profile")
class ProfileController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun profile(model: Model): String {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal is UserDetailsImpl) {
            val userDetail = authentication.principal as UserDetailsImpl
            val user = userService.findById(userDetail.getId()!!)?.get()
            if (user != null) {
                model.addAttribute("user", user)
                return "profile"
            }
        } else {
            return "redirect:/login"
        }
        return "profile"
    }

}