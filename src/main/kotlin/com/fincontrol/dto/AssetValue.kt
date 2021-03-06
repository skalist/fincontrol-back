package com.fincontrol.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fincontrol.model.AssetEvent
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
/**
 * Dto for registry table of asset values
 */
data class AssetValueListDto(
    val id: UUID,
    val assetName: String,
    val brokerAccountName: String,
    val eventName: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateCreated: LocalDate,
    val assetsCount: Int,
    val cost: BigDecimal,
)

/**
 * Dto for card of asset value
 */
data class AssetValueUpsertDto(
    val id: UUID?,
    val asset: AutocompleteOption<UUID>,
    val brokerAccount: AutocompleteOption<UUID>,
    val event: AutocompleteOption<AssetEvent>,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateCreated: LocalDate,
    val assetsCount: Int,
    val cost: BigDecimal,
)
