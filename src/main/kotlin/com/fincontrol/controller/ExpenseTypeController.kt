package com.fincontrol.controller

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.expense.type.ExpenseTypeListDto
import com.fincontrol.dto.expense.type.ExpenseTypeUpsertDto
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
    fun findAll(): List<ExpenseTypeListDto> = expenseTypeService.findAll()

    @GetMapping("{id}")
    fun findOne(@PathVariable id: UUID): ExpenseTypeUpsertDto = expenseTypeService.findOne(id)

    @PostMapping
    fun create(@RequestBody dto: ExpenseTypeUpsertDto): ExpenseTypeUpsertDto = expenseTypeService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: ExpenseTypeUpsertDto): ExpenseTypeUpsertDto = expenseTypeService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = expenseTypeService.delete(id)

    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = expenseTypeService.findSelects()
}
