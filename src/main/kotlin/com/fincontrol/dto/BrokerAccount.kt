package com.fincontrol.dto

import java.util.*

/**
 * Dto for registry table of broker accounts
 */
data class BrokerAccountListDto(
    val id: UUID,
    val name: String,
)

/**
 * Dto for card of broker account
 */
data class BrokerAccountUpsertDto(
    val id: UUID?,
    val name: String,
)
