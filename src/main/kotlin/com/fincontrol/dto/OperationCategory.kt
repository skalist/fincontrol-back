package com.fincontrol.dto

import java.util.*

/**
 * Dto for registry table of operation categories
 */
data class OperationCategoryListDto(
    val id: UUID,
    val name: String,
)

/**
 * Dto for card of operation category
 */
data class OperationCategoryUpsertDto(
    val id: UUID?,
    val name: String,
)
