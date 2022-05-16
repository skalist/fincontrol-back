package com.fincontrol.controller

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.BankAccountListDto
import com.fincontrol.dto.BankAccountUpsertDto
import com.fincontrol.service.BankAccountService
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of bank accounts
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("bank-account")
class BankAccountController(
    private val bankAccountService: BankAccountService
) {
    /**
     * Getting list of bank accounts for registry
     * @return list of dtos
     */
    @GetMapping
    fun findAll(): List<BankAccountListDto> = bankAccountService.findAll()

    /**
     * Getting bank account by identifier
     * @param id identifier of entity
     * @return dto of bank account
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): BankAccountUpsertDto = bankAccountService.findById(id)

    /**
     * Create new bank account entity
     * @param dto of bank account entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: BankAccountUpsertDto): UUID = bankAccountService.create(dto)

    /**
     * Updating existing bank account entity
     * @param dto of bank account entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: BankAccountUpsertDto): UUID = bankAccountService.update(dto)

    /**
     * Delete bank account entity by identifier
     * @param id identifier of bank account entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = bankAccountService.delete(id)

    /**
     * Getting values of bank accounts for autocomplete field
     * @return autocomplete values
     */
    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = bankAccountService.findSelects()
}
