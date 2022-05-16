package com.fincontrol.controller

import com.fincontrol.dto.BankOperationUpsertDto
import com.fincontrol.filter.BankOperationFilter
import com.fincontrol.service.BankOperationService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of bank operations
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("bank-operation")
class BankOperationController(
    private val bankOperationService: BankOperationService,
) {
    /**
     * Getting list of bank operations for registry
     * @param filter filter for bank operations
     * @param pageable information about page
     * @return page of dtos
     */
    @GetMapping
    fun findAll(filter: BankOperationFilter, pageable: Pageable) = bankOperationService.findAll(filter, pageable)

    /**
     * Getting entity of bank operation by identifier
     * @param id identifier of entity
     * @return dto of bank operation
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): BankOperationUpsertDto = bankOperationService.findById(id)

    /**
     * Create new bank operation entity
     * @param dto of bank operation entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: BankOperationUpsertDto): UUID = bankOperationService.create(dto)

    /**
     * Updating existing bank operation entity
     * @param dto of bank operation entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: BankOperationUpsertDto): UUID = bankOperationService.update(dto)

    /**
     * Delete bank operation entity by identifier
     * @param id identifier of bank operation entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = bankOperationService.delete(id)
}
