package br.com.gremiorupestre.grer.repository

import br.com.gremiorupestre.grer.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long>{



}