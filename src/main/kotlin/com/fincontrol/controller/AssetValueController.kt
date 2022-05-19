package com.fincontrol.controller

import com.fincontrol.dto.AssetValueListDto
import com.fincontrol.dto.AssetValueUpsertDto
import com.fincontrol.service.AssetValueService
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller for registry of asset values
 * Implemented CRUD operations
 */
@RestController
@RequestMapping("asset-value")
class AssetValueController(
    private val assetValueService: AssetValueService,
) {
    /**
     * Getting list of asset values for registry
     * @return list of dtos
     */
    @GetMapping
    fun findAll(): List<AssetValueListDto> = assetValueService.findAll()

    /**
     * Getting entity of asset value by identifier
     * @param id identifier of entity
     * @return dto of asset
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID):AssetValueUpsertDto = assetValueService.findById(id)

    /**
     * Create new asset value entity
     * @param dto of asset value entity
     * @return identifier of new entity
     */
    @PostMapping
    fun create(@RequestBody dto: AssetValueUpsertDto): UUID = assetValueService.create(dto)

    /**
     * Updating existing asset value entity
     * @param dto of asset value entity
     * @return identifier of updated entity
     */
    @PutMapping
    fun update(@RequestBody dto: AssetValueUpsertDto): UUID = assetValueService.update(dto)

    /**
     * Delete asset value entity by identifier
     * @param id identifier of asset entity
     */
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = assetValueService.delete(id)
}
