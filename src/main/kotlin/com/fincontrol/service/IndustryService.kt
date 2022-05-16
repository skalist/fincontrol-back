package com.fincontrol.service

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.IndustryListDto
import com.fincontrol.dto.IndustryUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.Industry
import com.fincontrol.repository.IndustryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service for registry of industries
 * Implemented CRUD operations
 */
@Service
@Transactional(readOnly = true)
class IndustryService(
    private val authenticationFacade: AuthenticationFacade,
    private val industryRepository: IndustryRepository,
) {
    /**
     * Getting list of industries for registry
     * @return list of dtos
     */
    fun findAll(): List<IndustryListDto> {
        val userId = authenticationFacade.getUserId()
        return industryRepository.findAllByUserId(userId).map { IndustryListDto(it.id, it.name) }
    }

    /**
     * Getting entity of industry by identifier
     * @param id identifier of entity
     * @return dto of industry
     */
    fun findById(id: UUID) = industryRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(Industry::class.java.simpleName, id) }
        .let { IndustryUpsertDto(it.id, it.name) }

    /**
     * Create new industry entity
     * @param dto of industry entity
     * @return identifier of new entity
     */
    @Transactional
    fun create(dto: IndustryUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val industry = Industry(userId = userId, name = dto.name)
        return industryRepository.save(industry).id
    }

    /**
     * Updating existing industry entity
     * @param dto of industry entity
     * @return identifier of updated entity
     */
    @Transactional
    fun update(dto: IndustryUpsertDto): UUID {
        val industry = industryRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(Industry::class.java.simpleName, dto.id) }
        val copiedIndustry = industry.copy(name = dto.name)
        return industryRepository.save(copiedIndustry).id
    }

    /**
     * Delete industry entity by identifier
     * @param id identifier of industry entity
     */
    @Transactional
    fun delete(id: UUID) {
        industryRepository.deleteById(id)
    }

    /**
     * Getting values of industries for autocomplete field
     * @return autocomplete values
     */
    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return industryRepository.findAllByUserId(userId).map { AutocompleteOption(it.id, it.name) }
    }
}
