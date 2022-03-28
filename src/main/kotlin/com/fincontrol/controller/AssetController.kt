package com.fincontrol.controller

import com.fincontrol.dto.AssetListDto
import com.fincontrol.dto.AssetUpsertDto
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.service.AssetService
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
@RequestMapping("asset")
class AssetController(
    private val assetService: AssetService,
) {
    @GetMapping
    fun findAll(): List<AssetListDto> = assetService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID):AssetUpsertDto = assetService.findById(id)

    @PostMapping
    fun create(@RequestBody dto: AssetUpsertDto): UUID = assetService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: AssetUpsertDto): UUID = assetService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = assetService.delete(id)

    @GetMapping("selects")
    fun findSelects(): List<AutocompleteOption<UUID>> = assetService.findSelects()
}
