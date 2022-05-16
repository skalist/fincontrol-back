package com.fincontrol.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fincontrol.model.OperationType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

/**
 * Dto for registry table of bank operations
 */
data class BankOperationListDto(
    val id: UUID,
    val operationCategoryName: String,
    val bankAccountName: String,
    val type: OperationType,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)

/**
 * Dto for card of bank operation
 */
data class BankOperationUpsertDto(
    val id: UUID?,
    val operationCategory: AutocompleteOption<UUID>,
    val bankAccount: AutocompleteOption<UUID>,
    val type: OperationType,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)
