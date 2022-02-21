package com.fincontrol.service

import com.fincontrol.dto.bank.account.BankAccountListDto
import com.fincontrol.dto.bank.account.BankAccountUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.BankAccount
import com.fincontrol.repository.BankAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    fun findAll(): List<BankAccountListDto> {
        val userId = authenticationFacade.getUserId()
        return bankAccountRepository.findAllByUserId(userId)
            .map { BankAccountListDto(it.id, it.name) }
    }

    fun findOne(id: UUID) = bankAccountRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(BankAccount::class.java.simpleName, id) }
        .let { BankAccountUpsertDto(it.id, it.name) }

    @Transactional
    fun create(dto: BankAccountUpsertDto): BankAccountUpsertDto {
        val userId = authenticationFacade.getUserId()
        val bankAccount = BankAccount(name = dto.name, userId = userId)
        return bankAccountRepository.save(bankAccount)
            .let { BankAccountUpsertDto(it.id, it.name) }
    }

    @Transactional
    fun update(dto: BankAccountUpsertDto): BankAccountUpsertDto {
        val bankAccount = bankAccountRepository.findById(dto.id!!).orElseThrow {
            throw EntityNotFoundException(BankAccount::class.java.simpleName, dto.id)
        }
        val copyBankAccount = bankAccount.copy(name = dto.name)

        return bankAccountRepository.save(copyBankAccount)
            .let { BankAccountUpsertDto(it.id, it.name) }
    }

    @Transactional
    fun delete(id: UUID) {
        bankAccountRepository.deleteById(id)
    }
}
