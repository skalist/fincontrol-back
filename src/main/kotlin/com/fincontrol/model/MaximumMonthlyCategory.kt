package com.fincontrol.model

import java.math.BigDecimal

data class MaximumMonthlyCategory(
    val category: OperationCategory,
    val sumCosts: BigDecimal,
)
