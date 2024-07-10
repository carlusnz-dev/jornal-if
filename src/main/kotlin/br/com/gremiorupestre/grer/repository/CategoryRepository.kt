package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
@EnableJpaRepositories
interface CategoryRepository : JpaRepository<Category, Long> {
}