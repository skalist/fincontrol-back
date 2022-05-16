package com.fincontrol.model

import java.math.BigDecimal

/**
 * Model for getting expense bank operation statistic by category, divided by month
 */
data class BankOperationStatisticByCategory(
    val month: Int,
    val year: Int,
    val value: BigDecimal,
)
