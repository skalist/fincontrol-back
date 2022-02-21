package com.fincontrol.service

import com.fincontrol.config.auth.UserPrincipal
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.expense.type.ExpenseTypeListDto
import com.fincontrol.dto.expense.type.ExpenseTypeUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.ExpenseType
import com.fincontrol.repository.ExpenseTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ExpenseTypeService(
    private val expenseTypeRepository: ExpenseTypeRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    fun findAll(): List<ExpenseTypeListDto> {
        val userId = authenticationFacade.getUserId()
        return expenseTypeRepository.findAllByUserId(userId)
            .map { ExpenseTypeListDto(it.id, it.name) }
    }

    fun findOne(id: UUID) = expenseTypeRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(ExpenseType::class.java.simpleName, id) }
        .let { ExpenseTypeUpsertDto(it.id, it.name) }

    @Transactional
    fun create(dto: ExpenseTypeUpsertDto): ExpenseTypeUpsertDto {
        val userId = authenticationFacade.getUserId()
        val expenseType = ExpenseType(name = dto.name, userId = userId)
        return expenseTypeRepository.save(expenseType)
            .let { ExpenseTypeUpsertDto(it.id, it.name) }
    }

    @Transactional
    fun update(dto: ExpenseTypeUpsertDto): ExpenseTypeUpsertDto {
        val expenseType = expenseTypeRepository.findById(dto.id!!).orElseThrow {
            throw EntityNotFoundException(ExpenseType::class.java.simpleName, dto.id)
        }
        val copyExpenseType = expenseType.copy(name = dto.name)

        return expenseTypeRepository.save(copyExpenseType)
            .let { ExpenseTypeUpsertDto(it.id, it.name) }
    }

    @Transactional
    fun delete(id: UUID) {
        expenseTypeRepository.deleteById(id)
    }

    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return expenseTypeRepository.findAllByUserId(userId)
            .map { AutocompleteOption(it.id, it.name) }
    }
}
