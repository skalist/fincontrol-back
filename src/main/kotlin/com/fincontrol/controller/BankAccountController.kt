package com.fincontrol.controller

import com.fincontrol.dto.bank.account.BankAccountListDto
import com.fincontrol.dto.bank.account.BankAccountUpsertDto
import com.fincontrol.dto.expense.type.ExpenseTypeListDto
import com.fincontrol.dto.expense.type.ExpenseTypeUpsertDto
import com.fincontrol.service.BankAccountService
import com.fincontrol.service.ExpenseTypeService
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
@RequestMapping("bank-account")
class BankAccountController(
    private val bankAccountService: BankAccountService
) {
    @GetMapping
    fun findAll(): List<BankAccountListDto> = bankAccountService.findAll()

    @GetMapping("{id}")
    fun findOne(@PathVariable id: UUID): BankAccountUpsertDto = bankAccountService.findOne(id)

    @PostMapping
    fun create(@RequestBody dto: BankAccountUpsertDto): BankAccountUpsertDto = bankAccountService.create(dto)

    @PutMapping
    fun update(@RequestBody dto: BankAccountUpsertDto): BankAccountUpsertDto = bankAccountService.update(dto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) = bankAccountService.delete(id)
}
