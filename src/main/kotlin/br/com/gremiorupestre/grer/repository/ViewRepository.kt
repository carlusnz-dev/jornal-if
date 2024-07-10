package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.View
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface ViewRepository : JpaRepository<View, Long> {
}