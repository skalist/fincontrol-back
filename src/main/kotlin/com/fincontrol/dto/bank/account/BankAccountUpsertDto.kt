package com.fincontrol.dto.bank.account

import java.util.UUID

data class BankAccountUpsertDto(
    val id: UUID?,
    val name: String,
)
