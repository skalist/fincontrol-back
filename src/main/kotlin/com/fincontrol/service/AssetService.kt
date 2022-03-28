package com.fincontrol.service

import com.fincontrol.dto.AssetListDto
import com.fincontrol.dto.AssetUpsertDto
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.Asset
import com.fincontrol.model.Industry
import com.fincontrol.repository.AssetRepository
import com.fincontrol.repository.IndustryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AssetService(
    private val assetRepository: AssetRepository,
    private val authenticationFacade: AuthenticationFacade,
    private val industryRepository: IndustryRepository,
) {
    fun findAll(): List<AssetListDto> {
        val userId = authenticationFacade.getUserId()
        return assetRepository.findAllByUserId(userId).map {
            AssetListDto(
                id = it.id,
                name = it.name,
                type = it.type.value,
                currency = it.currency.value,
                code = it.code,
                description = it.description,
                industryName = it.industry?.name
            )
        }
    }

    fun findById(id: UUID) = assetRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(Asset::class.java.simpleName, id) }
        .let {
            AssetUpsertDto(
                id = it.id,
                name = it.name,
                type = AutocompleteOption(it.type, it.type.value),
                currency = AutocompleteOption(it.currency, it.currency.value),
                code = it.code,
                description = it.description,
                industry = if (it.industry != null) AutocompleteOption(it.industry.id, it.industry.name) else null
            )
        }

    @Transactional
    fun create(dto: AssetUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val industry = if (dto.industry != null) {
            industryRepository.findById(dto.industry.value)
                .orElseThrow { throw EntityNotFoundException(Industry::class.java.simpleName, dto.industry.value) }
        } else {
            null
        }
        val asset = Asset(
            userId = userId,
            name = dto.name,
            type = dto.type.value,
            currency = dto.currency.value,
            code = dto.code,
            description = dto.description,
            industry = industry,
        )

        return assetRepository.save(asset).id
    }

    @Transactional
    fun update(dto: AssetUpsertDto): UUID {
        val asset = assetRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(Asset::class.java.simpleName, dto.id) }
        val industry = if (dto.industry != null) {
            industryRepository.findById(dto.industry.value)
                .orElseThrow { throw EntityNotFoundException(Industry::class.java.simpleName, dto.industry.value) }
        } else {
            null
        }

        val copiedAsset = asset.copy(
            name = dto.name,
            type = dto.type.value,
            currency = dto.currency.value,
            code = dto.code,
            description = dto.description,
            industry = industry,
        )

        return assetRepository.save(copiedAsset).id
    }

    @Transactional
    fun delete(id: UUID) = assetRepository.deleteById(id)

    fun findSelects(): List<AutocompleteOption<UUID>> {
        val userId = authenticationFacade.getUserId()
        return assetRepository.findAllByUserId(userId).map { AutocompleteOption(it.id, it.name) }
    }
}
