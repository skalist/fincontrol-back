package com.fincontrol.model

import java.math.BigDecimal

data class CostByCategory(
    val category: OperationCategory,
    val sumCosts: BigDecimal,
)
