package com.fincontrol.dto

import com.fincontrol.model.AssetType
import com.fincontrol.model.Currency
import java.util.*

data class AssetListDto(
    val id: UUID,
    val name: String,
    val type: AssetType,
    val currency: Currency,
    val code: String?,
    val description: String?,
    val industryName: String?,
)

data class AssetUpsertDto(
    val id: UUID,
    val name: String,
    val type: AssetType,
    val currency: Currency,
    val code: String?,
    val description: String?,
    val industry: AutocompleteOption<UUID>?,
)
