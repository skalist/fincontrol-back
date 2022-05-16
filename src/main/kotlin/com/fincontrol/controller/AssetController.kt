package com.fincontrol.controller

import com.fincontrol.dto.AssetListDto
import com.fincontrol.dto.AssetUpsertDto
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.service.AssetService
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of assets
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("asset")
class AssetController(
    private val assetService: AssetService,
) {
    /**
     * Getting list of assets for registry
     * @return list of dtos
     */
    @GetMapping
    fun findAll(): List<AssetListDto> = assetService.findAll()

    /**
     * Getting entity of asset by identifier
     * @param id identifier of entity
     * @return dto of asset
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID):AssetUpsertDto = assetService.findById(id)

    /**
     * Create new asset entity
     * @param dto of asset entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: AssetUpsertDto): UUID = assetService.create(dto)

    /**
     * Updating existing asset entity
     * @param dto of asset entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: AssetUpsertDto): UUID = assetService.update(dto)

    /**
     * Delete asset entity by identifier
     * @param id identifier of asset entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = assetService.delete(id)

    /**
     * Getting values of assets for autocomplete field
     * @return autocomplete values
     */
    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = assetService.findSelects()
}
