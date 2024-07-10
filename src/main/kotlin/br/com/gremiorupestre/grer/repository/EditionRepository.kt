package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.Edition
import jakarta.persistence.Entity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface EditionRepository : JpaRepository<Edition, Long>{
}