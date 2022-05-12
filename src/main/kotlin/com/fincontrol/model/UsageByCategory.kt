package com.fincontrol.model

data class UsageByCategory(
    val category: OperationCategory,
    val countUsage: Long,
)
