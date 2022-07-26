package com.fincontrol.dto

import com.fincontrol.model.BrokerAccountType
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

/**
 * Dto for getting accounts by token
 */
data class BrokerAccountByTokenDto(
    val externalId: String,
    val name: String,
    val type: BrokerAccountType,
)
