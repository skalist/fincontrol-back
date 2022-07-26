package com.fincontrol.service.investment

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.BrokerAccountByTokenDto
import com.fincontrol.dto.BrokerAccountListDto
import com.fincontrol.dto.BrokerAccountUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.BrokerAccount
import com.fincontrol.model.BrokerAccountType
import com.fincontrol.repository.BrokerAccountRepository
import com.fincontrol.service.AuthenticationFacade
import com.fincontrol.util.decodeName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tinkoff.piapi.contract.v1.*
import java.util.*

/**
 * Service for registry of broker account
 * Implemented CRUD operations
 */
@Service
@Transactional(readOnly = true)
class BrokerAccountService(
    private val brokerAccountRepository: BrokerAccountRepository,
    private val authenticationFacade: AuthenticationFacade,
    private val tinkoffInvestmentChannel: TinkoffInvestmentChannel,
) {
    /**
     * Getting list of broker accounts for registry
     * @return list of dtos
     */
    fun findAll(): List<BrokerAccountListDto> {
        val userId = authenticationFacade.getUserId()
        return brokerAccountRepository.findAllByUserId(userId)
            .map { BrokerAccountListDto(it.id, it.name) }
    }

    /**
     * Getting entity of broker account by identifier
     * @param id identifier of entity
     * @return dto of broker account
     */
    fun findById(id: UUID) = brokerAccountRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(BrokerAccount::class.java.simpleName, id) }
        .let { BrokerAccountUpsertDto(it.id, it.name) }

    /**
     * Create new broker account entity
     * @param dto of broker account entity
     * @return identifier of new entity
     */
    @Transactional
    fun create(dto: BrokerAccountUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val brokerAccount = BrokerAccount(userId = userId, name = dto.name)
        return brokerAccountRepository.save(brokerAccount).id
    }

    /**
     * Updating existing broker account entity
     * @param dto of broker account entity
     * @return identifier of updated entity
     */
    @Transactional
    fun update(dto: BrokerAccountUpsertDto): UUID {
        val brokerAccount = brokerAccountRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(BrokerAccount::class.java.simpleName, dto.id) }
        val copiedBrokerAccount = brokerAccount.copy(name = dto.name)
        return brokerAccountRepository.save(copiedBrokerAccount).id
    }

    /**
     * Delete broker account entity by identifier
     * @param id identifier of broker account entity
     */
    @Transactional
    fun delete(id: UUID) = brokerAccountRepository.deleteById(id)

    /**
     * Getting values of broker accounts for autocomplete field
     * @return autocomplete values
     */
    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return brokerAccountRepository.findAllByUserId(userId).map { AutocompleteOption(it.id, it.name) }
    }

    /**
     * Getting accounts by token
     * @param token provided token
     * @return list of available accounts
     */
    suspend fun getAccountsByToken(token: String): List<BrokerAccountByTokenDto> {
        val channel = tinkoffInvestmentChannel.getChannel(token)
        val userService = UsersServiceGrpcKt.UsersServiceCoroutineStub(channel)
        val accounts = userService.getAccounts(getAccountsRequest { })
        return accounts.accountsList
            .filter {
                setOf(
                    AccessLevel.ACCOUNT_ACCESS_LEVEL_FULL_ACCESS,
                    AccessLevel.ACCOUNT_ACCESS_LEVEL_READ_ONLY
                ).contains(it.accessLevel)
            }
            .map { it.toBrokerAccountByTokenDto() }
    }

    /**
     * Convert Account dto to BrokerAccountByTokenDto
     */
    fun Account.toBrokerAccountByTokenDto(): BrokerAccountByTokenDto {
        val type = if (type == AccountType.ACCOUNT_TYPE_TINKOFF_IIS) {
            BrokerAccountType.IIS
        } else {
            BrokerAccountType.STANDARD
        }
        return BrokerAccountByTokenDto(id, decodeName(name), type)
    }
}
