package com.fincontrol.dto

import java.util.*

/**
 * Dto for registry table of bank accounts
 */
data class BankAccountListDto(
    val id: UUID,
    val name: String,
)

/**
 * Dto for card of bank account
 */
data class BankAccountUpsertDto(
    val id: UUID?,
    val name: String,
)
