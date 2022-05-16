package com.fincontrol.controller

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.OperationCategoryListDto
import com.fincontrol.dto.OperationCategoryUpsertDto
import com.fincontrol.service.OperationCategoryService
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of operation categories
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("operation-category")
class OperationCategoryController(
    private val operationCategoryService: OperationCategoryService
) {
    /**
     * Getting list of operation categories for registry
     * @return list of dtos
     */
    @GetMapping
    fun findAll(): List<OperationCategoryListDto> = operationCategoryService.findAll()

    /**
     * Getting entity of operation category by identifier
     * @param id identifier of entity
     * @return dto of operation category
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): OperationCategoryUpsertDto = operationCategoryService.findById(id)

    /**
     * Create new operation category entity
     * @param dto of operation category entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: OperationCategoryUpsertDto): UUID = operationCategoryService.create(dto)

    /**
     * Updating existing operation category entity
     * @param dto of operation category entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: OperationCategoryUpsertDto): UUID = operationCategoryService.update(dto)

    /**
     * Delete operation category entity by identifier
     * @param id identifier of operation category entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = operationCategoryService.delete(id)

    /**
     * Getting values of operation category for autocomplete field
     * @return autocomplete values
     */
    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = operationCategoryService.findSelects()
}
