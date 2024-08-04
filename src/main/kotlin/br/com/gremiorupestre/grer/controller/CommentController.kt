package br.com.gremiorupestre.grer.controller

import br.com.gremiorupestre.grer.model.Comment
import br.com.gremiorupestre.grer.security.userdetails.UserDetailsImpl
import br.com.gremiorupestre.grer.service.ArticleService
import br.com.gremiorupestre.grer.service.CommentService
import br.com.gremiorupestre.grer.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/comments")
class CommentController {

    @Autowired
    lateinit var commentService: CommentService

    @Autowired
    lateinit var articleService: ArticleService

    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun findAll() = commentService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = commentService.findById(id)

    @PostMapping("/save/{articleID}")
    fun save(@ModelAttribute comment: Comment, @PathVariable articleID: Long): String {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal is UserDetailsImpl) {
            val userDetail = authentication.principal as UserDetailsImpl
            val user = userService.findById(userDetail.getId()!!)!!.get()
            if (user != null) {
                comment.user = user
            } else {
                throw IllegalArgumentException("User not found")
            }
        } else {
            throw IllegalArgumentException("User not authenticated")
        }
        val article = articleService.findById(articleID).orElseThrow { IllegalArgumentException("Article not found") }
        comment.article = article
        println("Comment: $comment")
        commentService.save(comment)
        return "redirect:/articles/${articleID}"
    }

    @GetMapping("/delete/{commentID}")
    fun delete(@PathVariable commentID: Long): String {
        val comment = commentService.findById(commentID).orElseThrow { IllegalArgumentException("Comment not found") }
        val articleID = comment.article.id
        commentService.delete(comment)
        return "redirect:/articles/${articleID}"
    }

}