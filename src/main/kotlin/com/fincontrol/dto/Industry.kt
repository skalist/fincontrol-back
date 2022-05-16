package com.fincontrol.dto

import java.util.*

/**
 * Dto for registry table of industries
 */
data class IndustryListDto(
    val id: UUID,
    val name: String,
)

/**
 * Dto for card of industry
 */
data class IndustryUpsertDto(
    val id: UUID?,
    val name: String,
)
