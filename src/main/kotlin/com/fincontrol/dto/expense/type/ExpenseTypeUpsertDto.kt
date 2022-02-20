package com.fincontrol.dto.expense.type

import java.util.UUID

data class ExpenseTypeUpsertDto(
    val id: UUID?,
    val name: String,
)
