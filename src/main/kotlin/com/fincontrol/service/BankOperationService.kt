package com.fincontrol.service

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.BankOperationListDto
import com.fincontrol.dto.BankOperationUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.BankOperation
import com.fincontrol.repository.BankAccountRepository
import com.fincontrol.repository.BankOperationRepository
import com.fincontrol.repository.ExpenseTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class BankOperationService(
    private val bankOperationRepository: BankOperationRepository,
    private val authenticationFacade: AuthenticationFacade,
    private val expenseTypeRepository: ExpenseTypeRepository,
    private val bankAccountRepository: BankAccountRepository,
) {
    fun findAll(): List<BankOperationListDto> {
        val userId = authenticationFacade.getUserId()
        return bankOperationRepository.findAllByUserIdOrderByDateCreatedDesc(userId)
            .map {
                BankOperationListDto(
                    id = it.id,
                    expenseTypeName = it.expenseType.name,
                    bankAccountName = it.bankAccount.name,
                    type = it.type,
                    dateCreated = it.dateCreated,
                    cost = it.cost,
                )
            }
    }

    fun findById(id: UUID): BankOperationUpsertDto {
        val bankOperation = bankOperationRepository.findById(id)
            .orElseThrow { throw EntityNotFoundException(BankOperation::class.java.simpleName, id) }
        return bankOperation.let {
            BankOperationUpsertDto(
                id = it.id,
                expenseType = AutocompleteOption(it.expenseType.id, it.expenseType.name),
                bankAccount = AutocompleteOption(it.bankAccount.id, it.bankAccount.name),
                type = it.type,
                dateCreated = it.dateCreated,
                cost = it.cost
            )
        }
    }

    @Transactional
    fun create(dto: BankOperationUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val expenseType = expenseTypeRepository.findById(dto.expenseType.value).orElse(null)
        val bankAccount = bankAccountRepository.findById(dto.bankAccount.value).orElse(null)

        val bankOperation = BankOperation(
            userId = userId,
            expenseType = expenseType,
            bankAccount = bankAccount,
            type = dto.type,
            dateCreated = dto.dateCreated,
            cost = dto.cost
        )

        return bankOperationRepository.save(bankOperation).id
    }

    @Transactional
    fun update(dto: BankOperationUpsertDto): UUID {
        val expenseType = expenseTypeRepository.findById(dto.expenseType.value).orElse(null)
        val bankAccount = bankAccountRepository.findById(dto.bankAccount.value).orElse(null)

        val bankOperation = bankOperationRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(BankOperation::class.java.simpleName, dto.id) }

        val copyBankOperation = bankOperation.copy(
            expenseType = expenseType,
            bankAccount = bankAccount,
            type = dto.type,
            dateCreated = dto.dateCreated,
            cost = dto.cost
        )

        return bankOperationRepository.save(copyBankOperation).id
    }

    @Transactional
    fun delete(id: UUID) {
        bankOperationRepository.deleteById(id)
    }
}
