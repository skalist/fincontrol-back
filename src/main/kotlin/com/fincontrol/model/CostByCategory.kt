package com.fincontrol.model

import java.math.BigDecimal

/**
 * Model for getting most expensive category
 */
data class CostByCategory(
    val category: OperationCategory,
    val sumCosts: BigDecimal,
)
