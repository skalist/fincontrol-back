package com.fincontrol.model

import java.math.BigDecimal

data class BankOperationStatisticByType(
    val type: OperationType,
    val month: Int,
    val value: BigDecimal,
)
