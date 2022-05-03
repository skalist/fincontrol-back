package com.fincontrol.controller

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.OperationCategoryListDto
import com.fincontrol.dto.OperationCategoryUpsertDto
import com.fincontrol.service.OperationCategoryService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("operation-category")
class OperationCategoryController(
    private val operationCategoryService: OperationCategoryService
) {
    @GetMapping
    fun findAll(): List<OperationCategoryListDto> = operationCategoryService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): OperationCategoryUpsertDto = operationCategoryService.findById(id)

    @PostMapping
    fun create(@RequestBody dto: OperationCategoryUpsertDto): OperationCategoryUpsertDto = operationCategoryService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: OperationCategoryUpsertDto): OperationCategoryUpsertDto = operationCategoryService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = operationCategoryService.delete(id)

    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = operationCategoryService.findSelects()
}
