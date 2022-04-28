package com.fincontrol.controller

import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseValueStatisticByCategoryFilter
import com.fincontrol.service.BankOperationStatisticService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("bank-operation-statistic")
class BankOperationStatisticController(
    val bankOperationStatisticService: BankOperationStatisticService,
) {
    @GetMapping("by-type")
    fun getOperationStatisticByType(filter: BankOperationStatisticByTypeFilter) =
        bankOperationStatisticService.getBankOperationStatisticByType(filter)

    @GetMapping("expense-by-category")
    fun getExpenseValueStatisticByCategory(filter: ExpenseValueStatisticByCategoryFilter) =
        bankOperationStatisticService.getBankOperationStatisticByCategory(filter)

    @GetMapping("/last-month")
    fun getLastFilledMonth() = bankOperationStatisticService.getLastFilledMonth()
}