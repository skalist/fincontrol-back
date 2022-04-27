package com.fincontrol.model

import java.math.BigDecimal

data class BankOperationStatisticByCategory(
    val categoryName: String,
    val value: BigDecimal,
)
