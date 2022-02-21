package com.fincontrol.controller

import com.fincontrol.dto.BankOperationListDto
import com.fincontrol.dto.BankOperationUpsertDto
import com.fincontrol.service.BankOperationService
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
@RequestMapping("bank-operation")
class BankOperationController(
    private val bankOperationService: BankOperationService,
) {
    @GetMapping
    fun findAll(): List<BankOperationListDto> = bankOperationService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): BankOperationUpsertDto = bankOperationService.findById(id)

    @PostMapping
    fun create(@RequestBody dto: BankOperationUpsertDto): UUID = bankOperationService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: BankOperationUpsertDto): UUID = bankOperationService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = bankOperationService.delete(id)
}
