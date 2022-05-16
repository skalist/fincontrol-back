package com.fincontrol.model

/**
 * Model for getting most usable category
 */
data class UsageByCategory(
    val category: OperationCategory,
    val countUsage: Long,
)
