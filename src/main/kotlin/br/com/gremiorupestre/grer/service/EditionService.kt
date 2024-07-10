package br.com.gremiorupestre.grer.service

import br.com.gremiorupestre.grer.model.Edition
import br.com.gremiorupestre.grer.repository.EditionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class EditionService {

    @Autowired
    lateinit var editionRepository: EditionRepository

    fun findAll(): List<Edition> {
        return editionRepository.findAll()
    }

    fun findById(id: Long): Optional<Edition> {
        return editionRepository.findById(id)
    }

    fun save(
        edition: Edition
    ): Edition {
        return editionRepository.save(edition)
    }

    fun deleteById(id: Long) {
        editionRepository.deleteById(id)
    }

    fun updateEdition(id: Long, updatedEdition: Edition): Optional<Edition> {
        return editionRepository.findById(id).map { existingEdition ->
            val editionToSave = existingEdition.copy(
                name = updatedEdition.name,
                number = updatedEdition.number,
                description = updatedEdition.description,
                date = updatedEdition.date,
                articles = updatedEdition.articles
            )
            editionRepository.save(editionToSave)
        }
    }

}