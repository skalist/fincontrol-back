package com.fincontrol.dto

import com.fincontrol.model.AssetType
import com.fincontrol.model.Currency
import java.util.*

/**
 * Dto for registry table of assets
 */
data class AssetListDto(
    val id: UUID,
    val name: String,
    val type: String,
    val currency: String,
    val code: String?,
    val description: String?,
    val industryName: String?,
)

/**
 * Dto for card of asset
 */
data class AssetUpsertDto(
    val id: UUID?,
    val name: String,
    val type: AutocompleteOption<AssetType>,
    val currency: AutocompleteOption<Currency>,
    val code: String?,
    val description: String?,
    val industry: AutocompleteOption<UUID>?,
)
