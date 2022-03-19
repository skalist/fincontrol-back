package com.fincontrol.dto

import java.util.*

data class BankAccountListDto(
    val id: UUID,
    val name: String,
)

data class BankAccountUpsertDto(
    val id: UUID?,
    val name: String,
)
