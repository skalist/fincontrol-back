package com.fincontrol.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fincontrol.model.OperationType
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Dto for bank operation statistic
 * Describes monthly expenses and outcomes
 */
data class BankOperationStatisticByTypeDto(
    /**
     * List of month, where values represented. Contains chain of month between min and max months
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    val months: List<LocalDate>,
    /**
     * Values of monthly statistic, grouped by type
     */
    val series: Map<OperationType, List<BigDecimal>>,
)

/**
 * Bank operation statistic of expense. Contains monthly statistic divided by category
 */
data class MonthlyExpenseStatisticByCategoryDto(
    /**
     * List of most expensive categories
     */
    val labels: List<String>,
    /**
     * Values of most expensive categories
     */
    val series: List<BigDecimal>,
    /**
     * Sum values of other categories
     */
    val other: BigDecimal,
)

/**
 * Last month, which contains expense data
 */
data class LastMonthOfBankOperation(
    val month: Int,
    val year: Int,
)

/**
 * Bank operation statistic of expense for selected period and category, divided by month
 */
data class ExpenseStatisticByCategoryDto(
    /**
     * List of month, where values represented. Contains chain of month between min and max months
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    val labels: List<LocalDate>,
    /**
     * Values of monthly statistic
     */
    val series: List<BigDecimal>,
)

/**
 * Bank operation statistic of expense. Represent median value of category for each month
 */
data class MedianBankOperationStatisticByCategoryDto(
    /**
     * List of month, where values represented.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    val month: LocalDate,
    /**
     * Values of statistic, contains 3 values min, middle and max value
     */
    val series: List<BigDecimal>
)
