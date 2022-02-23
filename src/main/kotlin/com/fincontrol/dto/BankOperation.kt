package com.fincontrol.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fincontrol.model.OperationType
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BankOperationListDto(
    val id: UUID,
    val expenseTypeName: String,
    val bankAccountName: String,
    val type: OperationType,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)

data class BankOperationUpsertDto(
    val id: UUID?,
    val expenseType: AutocompleteOption<UUID>,
    val bankAccount: AutocompleteOption<UUID>,
    val type: OperationType,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)
