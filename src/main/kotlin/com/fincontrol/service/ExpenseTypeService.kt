package com.fincontrol.service

import com.fincontrol.dto.ExpenseTypeListDto
import com.fincontrol.dto.ExpenseTypeUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.ExpenseType
import com.fincontrol.repository.ExpenseTypeRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExpenseTypeService(
    private val expenseTypeRepository: ExpenseTypeRepository
) {
    fun findAll() = expenseTypeRepository.findAll().map { ExpenseTypeListDto(it.id, it.name) }

    fun findOne(id: UUID) = expenseTypeRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(ExpenseType::class.java.simpleName, id) }
        .let { ExpenseTypeUpsertDto(it.id, it.name) }

    fun create(dto: ExpenseTypeUpsertDto): ExpenseTypeUpsertDto {
        val expenseType = ExpenseType(name = dto.name)
        return expenseTypeRepository.save(expenseType)
            .let { ExpenseTypeUpsertDto(it.id, it.name) }
    }

    fun update(dto: ExpenseTypeUpsertDto): ExpenseTypeUpsertDto {
        val expenseType = expenseTypeRepository.findById(dto.id!!).orElseThrow {
            throw EntityNotFoundException(ExpenseType::class.java.simpleName, dto.id)
        }
        val copyExpenseType = expenseType.copy(name = dto.name)

        return expenseTypeRepository.save(copyExpenseType)
            .let { ExpenseTypeUpsertDto(it.id, it.name) }
    }

    fun delete(id: UUID) {
        expenseTypeRepository.deleteById(id)
    }
}
