package com.fincontrol.service

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.OperationCategoryListDto
import com.fincontrol.dto.OperationCategoryUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.OperationCategory
import com.fincontrol.repository.OperationCategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service for registry of operation categories
 * Implemented CRUD operations
 */
@Service
@Transactional(readOnly = true)
class OperationCategoryService(
    private val operationCategoryRepository: OperationCategoryRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    /**
     * Getting list of operation categories for registry
     * @return list of dtos
     */
    fun findAll(): List<OperationCategoryListDto> {
        val userId = authenticationFacade.getUserId()
        return operationCategoryRepository.findAllByUserIdOrderByName(userId)
            .map { OperationCategoryListDto(it.id, it.name) }
    }

    /**
     * Getting entity of operation category by identifier
     * @param id identifier of entity
     * @return dto of operation category
     */
    fun findById(id: UUID) = operationCategoryRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(OperationCategory::class.java.simpleName, id) }
        .let { OperationCategoryUpsertDto(it.id, it.name) }

    /**
     * Create new operation category entity
     * @param dto of operation category entity
     * @return identifier of new entity
     */
    @Transactional
    fun create(dto: OperationCategoryUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val operationCategory = OperationCategory(name = dto.name, userId = userId)
        return operationCategoryRepository.save(operationCategory).id
    }

    /**
     * Updating existing operation category entity
     * @param dto of operation category entity
     * @return identifier of updated entity
     */
    @Transactional
    fun update(dto: OperationCategoryUpsertDto): UUID {
        val operationCategory = operationCategoryRepository.findById(dto.id!!).orElseThrow {
            throw EntityNotFoundException(OperationCategory::class.java.simpleName, dto.id)
        }
        val copyOperationCategory = operationCategory.copy(name = dto.name)

        return operationCategoryRepository.save(copyOperationCategory).id
    }

    /**
     * Delete operation category entity by identifier
     * @param id identifier of operation category entity
     */
    @Transactional
    fun delete(id: UUID) {
        operationCategoryRepository.deleteById(id)
    }

    /**
     * Getting values of operation category for autocomplete field
     * @return autocomplete values
     */
    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return operationCategoryRepository.findAllByUserIdOrderByName(userId)
            .map { AutocompleteOption(it.id, it.name) }
    }
}
