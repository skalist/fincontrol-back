package com.fincontrol.service

import com.fincontrol.dto.BankAccountUpsertDto
import com.fincontrol.dto.BrokerAccountListDto
import com.fincontrol.dto.BrokerAccountUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.BrokerAccount
import com.fincontrol.repository.BrokerAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class BrokerAccountService(
    private val brokerAccountRepository: BrokerAccountRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    fun findAll(): List<BrokerAccountListDto> {
        val userId = authenticationFacade.getUserId()
        return brokerAccountRepository.findAllByUserId(userId)
            .map { BrokerAccountListDto(it.id, it.name) }
    }

    fun findById(id: UUID) = brokerAccountRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(BrokerAccount::class.java.simpleName, id) }
        .let { BrokerAccountUpsertDto(it.id, it.name) }

    @Transactional
    fun create(dto: BrokerAccountUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val brokerAccount = BrokerAccount(userId = userId, name = dto.name)
        return brokerAccountRepository.save(brokerAccount).id
    }

    @Transactional
    fun update(dto: BrokerAccountUpsertDto): UUID {
        val brokerAccount = brokerAccountRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(BrokerAccount::class.java.simpleName, dto.id) }
        val copiedBrokerAccount = brokerAccount.copy(name = dto.name)
        return brokerAccountRepository.save(copiedBrokerAccount).id
    }

    @Transactional
    fun delete(id: UUID) = brokerAccountRepository.deleteById(id)
}
