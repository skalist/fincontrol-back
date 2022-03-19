package com.fincontrol.controller

import com.fincontrol.dto.BankAccountUpsertDto
import com.fincontrol.dto.BrokerAccountListDto
import com.fincontrol.dto.BrokerAccountUpsertDto
import com.fincontrol.service.BrokerAccountService
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
@RequestMapping("broker-account")
class BrokerAccountController(
    private val brokerAccountService: BrokerAccountService,
) {
    @GetMapping
    fun findAll(): List<BrokerAccountListDto> = brokerAccountService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: UUID): BrokerAccountUpsertDto = brokerAccountService.findById(id)

    @PostMapping
    fun create(@RequestBody dto: BrokerAccountUpsertDto): UUID = brokerAccountService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: BrokerAccountUpsertDto): UUID = brokerAccountService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = brokerAccountService.delete(id)
}
