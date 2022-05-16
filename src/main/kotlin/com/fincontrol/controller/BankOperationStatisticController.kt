package com.fincontrol.controller

import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseStatisticByCategoryFilter
import com.fincontrol.filter.MedianStatisticByCategoryFilter
import com.fincontrol.filter.MonthlyExpenseStatisticByCategoryFilter
import com.fincontrol.service.BankOperationStatisticService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for getting statistic about bank operation
 */
@RestController
@RequestMapping("bank-operation-statistic")
class BankOperationStatisticController(
    val bankOperationStatisticService: BankOperationStatisticService,
) {
    /**
     * Get statistic by type divided into month
     * @param filter filter value
     * @return data statistic for apexCharts
     */
    @GetMapping("total-by-type")
    fun getTotalStatisticByType(filter: BankOperationStatisticByTypeFilter) =
        bankOperationStatisticService.getTotalStatisticByType(filter)

    /**
     * Get monthly statistic about expense divided into category
     * @param filter filter value
     * @return data statistic for apexChart
     */
    @GetMapping("monthly-expense-by-category")
    fun getMonthlyExpenseStatisticByCategory(filter: MonthlyExpenseStatisticByCategoryFilter) =
        bankOperationStatisticService.getMonthlyExpenseStatisticByCategory(filter)

    /**
     * Get last month with expense bank operations
     * @return month and year
     */
    @GetMapping("last-filled-month")
    fun getLastFilledMonth() = bankOperationStatisticService.getLastFilledMonth()

    /**
     * Get expense statistic by category and selected period divided into month
     * @param filter filter value
     * @return data statistic for apexChart
     */
    @GetMapping("expense-statistic-by-category")
    fun getExpenseStatisticByCategory(filter: ExpenseStatisticByCategoryFilter) =
        bankOperationStatisticService.getExpenseStatisticByCategory(filter)

    /**
     * Get the most expensive category for last year
     * @return category as dto for autocomplete
     */
    @GetMapping("most-expensive-category-for-last-year")
    fun getMostExpensiveCategoryForLastYear() = bankOperationStatisticService.getMostExpensiveCategoryForLastYear()

    /**
     * Get the most usable category for last year
     * @return category as dto for autocomplete
     */
    @GetMapping("most-usable-category-for-last-year")
    fun getMostUsableCategoryForLastYear() = bankOperationStatisticService.getMostUsableCategoryForLastYear()

    /**
     * Statistic, that describe minimum, maximum and middle value of category for selected period
     * @param filter filter value
     * @return data statistic for apexChart
     */
    @GetMapping("median-statistic-by-category")
    fun getMedianStatisticByCategory(filter: MedianStatisticByCategoryFilter) =
        bankOperationStatisticService.getMedianStatisticByCategory(filter)
}
