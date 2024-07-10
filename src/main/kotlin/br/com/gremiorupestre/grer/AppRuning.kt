package br.com.gremiorupestre.grer

import br.com.gremiorupestre.grer.model.Category
import br.com.gremiorupestre.grer.model.Role
import br.com.gremiorupestre.grer.model.RoleName
import br.com.gremiorupestre.grer.repository.CategoryRepository
import br.com.gremiorupestre.grer.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppRunning : ApplicationRunner {

    @Autowired
    @Transient
    private lateinit var roleRepository: RoleRepository

    @Autowired
    @Transient
    private lateinit var categoryRepository: CategoryRepository

    override fun run(args: ApplicationArguments?) {
        //createRoles()
        //createCategory()
    }

    fun createRoles(){
        val roleAdm = Role(id = 1, name = RoleName.ROLE_ADMIN )
        roleRepository.save(roleAdm)

        val roleUser= Role(id = 2, name = RoleName.ROLE_USER )
        roleRepository.save(roleUser)

        val roleNews= Role(id = 3, name = RoleName.ROLE_NEWS )
        roleRepository.save(roleNews)
    }

    fun createCategory(){

        // Category mutable list
        val categories = mutableListOf<Category>(
            Category(name = "Notícias", description = "Notícias do IFPI Campus São Raimundo Nonato"),
            Category(name = "Eventos", description = "Eventos do IFPI Campus São Raimundo Nonato"),
            Category(name = "Editais", description = "Editais do IFPI Campus São Raimundo Nonato"),
            Category(name = "Cursos", description = "Cursos do IFPI Campus São Raimundo Nonato"),
            Category(name = "Projetos", description = "Projetos do IFPI Campus São Raimundo Nonato"),
            Category(name = "Pesquisa", description = "Pesquisa do IFPI Campus São Raimundo Nonato"),
            Category(name = "Extensão", description = "Extensão do IFPI Campus São Raimundo Nonato"),
            Category(name = "Estágios", description = "Estágios do IFPI Campus São Raimundo Nonato")
        )

        categoryRepository.saveAll(categories)
    }

}