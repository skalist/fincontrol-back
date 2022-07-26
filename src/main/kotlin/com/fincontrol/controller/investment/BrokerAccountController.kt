package com.fincontrol.controller.investment

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.BrokerAccountByTokenDto
import com.fincontrol.dto.BrokerAccountListDto
import com.fincontrol.dto.BrokerAccountUpsertDto
import com.fincontrol.service.investment.BrokerAccountService
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of broker account
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("broker-account")
class BrokerAccountController(
    private val brokerAccountService: BrokerAccountService,
) {
    /**
     * Getting list of broker accounts for registry
     * @return list of dtos
     */
    @GetMapping
    fun findAll(): List<BrokerAccountListDto> = brokerAccountService.findAll()

    /**
     * Getting entity of broker account by identifier
     * @param id identifier of entity
     * @return dto of broker account
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): BrokerAccountUpsertDto = brokerAccountService.findById(id)

    /**
     * Create new broker account entity
     * @param dto of broker account entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: BrokerAccountUpsertDto): UUID = brokerAccountService.create(dto)

    /**
     * Updating existing broker account entity
     * @param dto of broker account entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: BrokerAccountUpsertDto): UUID = brokerAccountService.update(dto)

    /**
     * Delete broker account entity by identifier
     * @param id identifier of broker account entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = brokerAccountService.delete(id)

    /**
     * Getting values of broker accounts for autocomplete field
     * @return autocomplete values
     */
    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = brokerAccountService.findSelects()

    /**
     * Getting accounts by token
     * @param token provided token
     * @return list of available accounts
     */
    @GetMapping("by-token")
    suspend fun getAccountsByToken(@RequestParam token: String): List<BrokerAccountByTokenDto> {
        return brokerAccountService.getAccountsByToken(token)
    }
}
