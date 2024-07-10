package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.Category
import br.com.gremiorupestre.grer.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CategoryService {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }

    fun findById(id: Long): Optional<Category> {
        return categoryRepository.findById(id)
    }
}
