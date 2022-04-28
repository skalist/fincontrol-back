package com.fincontrol.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fincontrol.model.OperationType
import java.math.BigDecimal
import java.time.LocalDate

data class BankOperationStatisticByTypeDto(
    @JsonFormat(pattern = "yyyy-MM-dd")
    val months: List<LocalDate>,
    val series: Map<OperationType, List<BigDecimal>>,
)

data class BankOperationStatisticByCategoryDto(
    val labels: List<String>,
    val series: List<BigDecimal>,
    val other: BigDecimal,
)

data class LastMonthOfBankOperation(
    val month: Int,
    val year: Int,
)