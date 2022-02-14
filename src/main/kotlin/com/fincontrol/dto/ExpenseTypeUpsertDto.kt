package com.fincontrol.dto

import java.util.UUID

data class ExpenseTypeUpsertDto(
    val id: UUID?,
    val name: String,
)
