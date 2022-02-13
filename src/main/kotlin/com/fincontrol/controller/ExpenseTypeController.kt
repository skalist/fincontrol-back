package com.fincontrol.controller

import com.fincontrol.model.ExpenseType
import com.fincontrol.service.ExpenseTypeService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("expense-type")
class ExpenseTypeController(
    private val expenseTypeService: ExpenseTypeService
) {
    @GetMapping
    fun findAll() = expenseTypeService.findAll()

    @GetMapping("{id}")
    fun findOne(@PathVariable id: UUID) = expenseTypeService.findOne(id)

    @PostMapping
    fun create(@RequestBody expenseType: ExpenseType): ExpenseType {
        return expenseTypeService.save(expenseType)
    }

    @PutMapping
    fun update(@RequestBody expenseType: ExpenseType): ExpenseType {
        return expenseTypeService.save(expenseType)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) {
        expenseTypeService.delete(id)
    }
}
