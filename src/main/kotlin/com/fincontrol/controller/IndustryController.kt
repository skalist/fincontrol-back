package com.fincontrol.controller

import com.fincontrol.dto.IndustryListDto
import com.fincontrol.dto.IndustryUpsertDto
import com.fincontrol.service.IndustryService
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
@RequestMapping("industry")
class IndustryController(
    private val industryService: IndustryService,
) {
    @GetMapping
    fun findAll(): List<IndustryListDto> = industryService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): IndustryUpsertDto = industryService.findById(id)

    @PostMapping
    fun create(@RequestBody dto: IndustryUpsertDto): UUID = industryService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: IndustryUpsertDto): UUID = industryService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = industryService.delete(id)
}
