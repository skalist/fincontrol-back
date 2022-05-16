package com.fincontrol.service

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.BankAccountListDto
import com.fincontrol.dto.BankAccountUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.BankAccount
import com.fincontrol.repository.BankAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service for registry of bank accounts
 * Implemented CRUD operations
 */
@Service
@Transactional(readOnly = true)
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    /**
     * Getting list of bank accounts for registry
     * @return list of dtos
     */
    fun findAll(): List<BankAccountListDto> {
        val userId = authenticationFacade.getUserId()
        return bankAccountRepository.findAllByUserIdOrderByName(userId)
            .map { BankAccountListDto(it.id, it.name) }
    }

    /**
     * Getting bank account by identifier
     * @param id identifier of entity
     * @return dto of bank account
     */
    fun findById(id: UUID) = bankAccountRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(BankAccount::class.java.simpleName, id) }
        .let { BankAccountUpsertDto(it.id, it.name) }

    /**
     * Create new bank account entity
     * @param dto of bank account entity
     * @return identifier of new entity
     */
    @Transactional
    fun create(dto: BankAccountUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val bankAccount = BankAccount(name = dto.name, userId = userId)
        return bankAccountRepository.save(bankAccount).id
    }

    /**
     * Updating existing bank account entity
     * @param dto of bank account entity
     * @return identifier of updated entity
     */
    @Transactional
    fun update(dto: BankAccountUpsertDto): UUID {
        val bankAccount = bankAccountRepository.findById(dto.id!!).orElseThrow {
            throw EntityNotFoundException(BankAccount::class.java.simpleName, dto.id)
        }
        val copyBankAccount = bankAccount.copy(name = dto.name)

        return bankAccountRepository.save(copyBankAccount).id
    }

    /**
     * Delete bank account entity by identifier
     * @param id identifier of bank account entity
     */
    @Transactional
    fun delete(id: UUID) {
        bankAccountRepository.deleteById(id)
    }

    /**
     * Getting values of bank accounts for autocomplete field
     * @return autocomplete values
     */
    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return bankAccountRepository.findAllByUserIdOrderByName(userId)
            .map { AutocompleteOption(it.id, it.name) }
    }
}
