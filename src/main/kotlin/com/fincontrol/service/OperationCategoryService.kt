package com.fincontrol.service

import OperationCategoryListDto
import OperationCategoryUpsertDto
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.OperationCategory
import com.fincontrol.repository.OperationCategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class OperationCategoryService(
    private val operationCategoryRepository: OperationCategoryRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    fun findAll(): List<OperationCategoryListDto> {
        val userId = authenticationFacade.getUserId()
        return operationCategoryRepository.findAllByUserId(userId)
            .map { OperationCategoryListDto(it.id, it.name) }
    }

    fun findOne(id: UUID) = operationCategoryRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(OperationCategory::class.java.simpleName, id) }
        .let { OperationCategoryUpsertDto(it.id, it.name) }

    @Transactional
    fun create(dto: OperationCategoryUpsertDto): OperationCategoryUpsertDto {
        val userId = authenticationFacade.getUserId()
        val operationCategory = OperationCategory(name = dto.name, userId = userId)
        return operationCategoryRepository.save(operationCategory)
            .let { OperationCategoryUpsertDto(it.id, it.name) }
    }

    @Transactional
    fun update(dto: OperationCategoryUpsertDto): OperationCategoryUpsertDto {
        val operationCategory = operationCategoryRepository.findById(dto.id!!).orElseThrow {
            throw EntityNotFoundException(OperationCategory::class.java.simpleName, dto.id)
        }
        val copyOperationCategory = operationCategory.copy(name = dto.name)

        return operationCategoryRepository.save(copyOperationCategory)
            .let { OperationCategoryUpsertDto(it.id, it.name) }
    }

    @Transactional
    fun delete(id: UUID) {
        operationCategoryRepository.deleteById(id)
    }

    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return operationCategoryRepository.findAllByUserId(userId)
            .map { AutocompleteOption(it.id, it.name) }
    }
}
