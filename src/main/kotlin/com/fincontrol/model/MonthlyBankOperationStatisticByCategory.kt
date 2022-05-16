package com.fincontrol.model

import java.math.BigDecimal

/**
 * Model for getting monthly bank operation statistic about expense, divided by category
 */
data class MonthlyBankOperationStatisticByCategory(
    val categoryName: String,
    val value: BigDecimal,
)
