package com.fincontrol.service

import com.fincontrol.dto.AssetListDto
import com.fincontrol.dto.AssetUpsertDto
import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.Asset
import com.fincontrol.repository.AssetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AssetService(
    private val assetRepository: AssetRepository,
    private val authenticationFacade: AuthenticationFacade,
) {
    fun findAll(): List<AssetListDto> {
        val userId = authenticationFacade.getUserId()
        return assetRepository.findAllByUserId(userId).map {
            AssetListDto(
                id = it.id,
                name = it.name,
                type = it.type,
                currency = it.currency,
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
                type = it.type,
                currency = it.currency,
                code = it.code,
                description = it.description,
                industry = if (it.industry != null) AutocompleteOption(it.industry.id, it.industry.name) else null
            )
        }
}
