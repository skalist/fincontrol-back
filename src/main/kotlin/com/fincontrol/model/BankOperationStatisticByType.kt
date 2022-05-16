package com.fincontrol.model

import java.math.BigDecimal

/**
 * Model for getting bank operation statistic by type
 */
data class BankOperationStatisticByType(
    val type: OperationType,
    val month: Int,
    val year: Int,
    val value: BigDecimal,
)
