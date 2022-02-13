package com.fincontrol.service

import com.fincontrol.model.ExpenseType
import com.fincontrol.repository.ExpenseTypeRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExpenseTypeService(
    private val expenseTypeRepository: ExpenseTypeRepository
) {
    fun findAll(): List<ExpenseType> = expenseTypeRepository.findAll()

    fun findOne(id: UUID): ExpenseType = expenseTypeRepository.findById(id)
        .orElseThrow { throw Exception("Entity not found exception") }

    fun save(dto: ExpenseType): ExpenseType {
        return if (dto.id != null) {
            expenseTypeRepository.findById(dto.id!!)
                .orElseThrow { throw Exception("Entity not found exception") }
                .apply { name = dto.name }
                .let { expenseTypeRepository.save(it) }
        } else {
            expenseTypeRepository.save(dto)
        }
    }

    fun delete(id: UUID) {
        expenseTypeRepository.deleteById(id)
    }
}
