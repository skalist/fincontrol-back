package com.fincontrol.dto

import com.fincontrol.model.OperationType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BankOperationListDto(
    val id: UUID,
    val expenseTypeName: String,
    val bankAccountName: String,
    val type: OperationType,
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)

data class BankOperationUpsertDto(
    val id: UUID,
    val expenseType: AutocompleteOption<UUID>,
    val bankAccount: AutocompleteOption<UUID>,
    val type: OperationType,
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)
