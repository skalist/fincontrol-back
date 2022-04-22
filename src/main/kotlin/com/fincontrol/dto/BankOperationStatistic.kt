package com.fincontrol.dto

import com.fincontrol.model.OperationType
import java.math.BigDecimal

data class BankOperationStatisticByTypeDto(
    val months: List<Int>,
    val series: Map<OperationType, List<BigDecimal>>,
)
