package com.fincontrol.model

import java.math.BigDecimal

data class AnnualBankOperationStatisticByCategory(
    val month: Int,
    val year: Int,
    val value: BigDecimal,
)
