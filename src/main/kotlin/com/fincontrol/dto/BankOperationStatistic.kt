package com.fincontrol.dto

import com.fincontrol.model.OperationType
import java.math.BigDecimal

data class BankOperationStatisticByTypeDto(
    val months: List<Int>,
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
