package com.fincontrol.service

import com.fincontrol.dto.AssetValueListDto
import com.fincontrol.dto.AssetValueUpsertDto
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.Asset
import com.fincontrol.model.AssetValue
import com.fincontrol.model.BrokerAccount
import com.fincontrol.repository.AssetRepository
import com.fincontrol.repository.AssetValueRepository
import com.fincontrol.repository.BrokerAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service for registry of asset values
 * Implemented CRUD operations
 */
@Service
@Transactional(readOnly = true)
class AssetValueService(
    private val assetValueRepository: AssetValueRepository,
    private val authenticationFacade: AuthenticationFacade,
    private val assetRepository: AssetRepository,
    private val brokerAccountRepository: BrokerAccountRepository,
) {
    /**
     * Getting list of asset values for registry
     * @return list of dtos
     */
    fun findAll(): List<AssetValueListDto> {
        val userId = authenticationFacade.getUserId()
        return assetValueRepository.findAllByUserId(userId).map {
            AssetValueListDto(
                id = it.id,
                assetName = it.asset.name,
                brokerAccountName = it.brokerAccount.name,
                eventName = it.event.value,
                dateCreated = it.dateCreated,
                assetsCount = it.assetsCount,
                cost = it.cost
            )
        }
    }

    /**
     * Getting entity of asset value by identifier
     * @param id identifier of entity
     * @return dto of asset
     */
    fun findById(id: UUID) = assetValueRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(AssetValue::class.java.simpleName, id) }
        .let {
            AssetValueUpsertDto(
                id = it.id,
                asset = it.asset.let { asset -> AutocompleteOption(asset.id, asset.name) },
                brokerAccount = it.brokerAccount.let { account -> AutocompleteOption(account.id, account.name) },
                event = it.event.let { event -> AutocompleteOption(event, event.value) },
                dateCreated = it.dateCreated,
                assetsCount = it.assetsCount,
                cost = it.cost
            )
        }

    /**
     * Create new asset value entity
     * @param dto of asset value entity
     * @return identifier of new entity
     */
    @Transactional
    fun create(dto: AssetValueUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val asset = assetRepository.findById(dto.asset.value)
            .orElseThrow { throw EntityNotFoundException(Asset::class.java.simpleName, dto.asset.value) }
        val brokerAccount = brokerAccountRepository.findById(dto.brokerAccount.value)
            .orElseThrow {
                throw EntityNotFoundException(BrokerAccount::class.java.simpleName, dto.brokerAccount.value)
            }
        val assetValue = AssetValue(
            userId = userId,
            asset = asset,
            brokerAccount = brokerAccount,
            event = dto.event.value,
            dateCreated = dto.dateCreated,
            assetsCount = dto.assetsCount,
            cost = dto.cost,
        )

        return assetValueRepository.save(assetValue).id
    }

    /**
     * Updating existing asset value entity
     * @param dto of asset value entity
     * @return identifier of updated entity
     */
    @Transactional
    fun update(dto: AssetValueUpsertDto): UUID {
        val assetValue = assetValueRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(AssetValue::class.java.simpleName, dto.id) }
        val asset = assetRepository.findById(dto.asset.value)
            .orElseThrow { throw EntityNotFoundException(Asset::class.java.simpleName, dto.asset.value) }
        val brokerAccount = brokerAccountRepository.findById(dto.brokerAccount.value)
            .orElseThrow {
                throw EntityNotFoundException(BrokerAccount::class.java.simpleName, dto.brokerAccount.value)
            }
        val copiedAssetValue = assetValue.copy(
            asset = asset,
            brokerAccount = brokerAccount,
            event = dto.event.value,
            dateCreated = dto.dateCreated,
            assetsCount = dto.assetsCount,
            cost = dto.cost,
        )

        return assetValueRepository.save(copiedAssetValue).id
    }

    /**
     * Delete asset value entity by identifier
     * @param id identifier of asset entity
     */
    @Transactional
    fun delete(id: UUID) = assetValueRepository.deleteById(id)
}
