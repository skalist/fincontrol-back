package com.fincontrol.controller

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.IndustryListDto
import com.fincontrol.dto.IndustryUpsertDto
import com.fincontrol.service.IndustryService
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of industries
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("industry")
class IndustryController(
    private val industryService: IndustryService,
) {
    /**
     * Getting list of industries for registry
     * @return list of dtos
     */
    @GetMapping
    fun findAll(): List<IndustryListDto> = industryService.findAll()

    /**
     * Getting entity of industry by identifier
     * @param id identifier of entity
     * @return dto of industry
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): IndustryUpsertDto = industryService.findById(id)

    /**
     * Create new industry entity
     * @param dto of industry entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: IndustryUpsertDto): UUID = industryService.create(dto)

    /**
     * Updating existing industry entity
     * @param dto of industry entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: IndustryUpsertDto): UUID = industryService.update(dto)

    /**
     * Delete industry entity by identifier
     * @param id identifier of industry entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = industryService.delete(id)

    /**
     * Getting values of industries for autocomplete field
     * @return autocomplete values
     */
    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = industryService.findSelects()
}
