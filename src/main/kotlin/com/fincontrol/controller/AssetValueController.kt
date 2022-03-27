package com.fincontrol.controller

import com.fincontrol.dto.AssetValueListDto
import com.fincontrol.dto.AssetValueUpsertDto
import com.fincontrol.service.AssetValueService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("asset-value")
class AssetValueController(
    private val assetValueService: AssetValueService,
) {
    @GetMapping
    fun findAll(): List<AssetValueListDto> = assetValueService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID):AssetValueUpsertDto = assetValueService.findById(id)

    @PostMapping
    fun create(@RequestBody dto: AssetValueUpsertDto): UUID = assetValueService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: AssetValueUpsertDto): UUID = assetValueService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = assetValueService.delete(id)
}
