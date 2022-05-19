package com.fincontrol.controller

import com.fincontrol.ApplicationIT
import com.fincontrol.dsl.bankAccount
import com.fincontrol.dsl.bankOperation
import com.fincontrol.dsl.operationCategory
import com.fincontrol.dto.LastMonthOfBankOperation
import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseStatisticByCategoryFilter
import com.fincontrol.filter.MedianStatisticByCategoryFilter
import com.fincontrol.filter.MonthlyExpenseStatisticByCategoryFilter
import com.fincontrol.model.OperationType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class BankOperationStatisticControllerTest : ApplicationIT() {
    private lateinit var defaultBankAccount: Pair<UUID, String>
    private val expenseCategory: MutableList<Pair<UUID, String>> = mutableListOf()
    private lateinit var incomeCategory: Pair<UUID, String>

    @BeforeEach
    fun init() {
        val bankAccountId = bankAccountController.create(bankAccount { name("test account") }.build())
        defaultBankAccount = bankAccountId to "test account"

        repeat(11) {
            val expenseCategoryId =
                operationCategoryController.create(operationCategory { name("expense category$it") }.build())
            expenseCategory.add(expenseCategoryId to "expense category$it")
        }

        val incomeCategoryId = operationCategoryController.create(operationCategory { name("income category") }.build())
        incomeCategory = incomeCategoryId to "income category"
    }

    @Test
    fun `should return last filled month`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 2, 1))
            cost(BigDecimal(10))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 3, 1))
            cost(BigDecimal(10))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(incomeOperation)

        val lastFilledMonth = bankOperationStatisticController.getLastFilledMonth()
        assertThat(lastFilledMonth).isEqualTo(LastMonthOfBankOperation(2, 2000))
    }

    @Test
    fun `should return empty filled month`() {
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10))
        }.build()
        bankOperationController.create(incomeOperation)
        val lastFilledMonth = bankOperationStatisticController.getLastFilledMonth()
        assertThat(lastFilledMonth).isNull()
    }

    @Test
    fun `should return most expensive category for last year`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[1])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(incomeOperation)

        val mostExpensiveCategory = bankOperationStatisticController.getMostExpensiveCategoryForLastYear()
        assertThat(mostExpensiveCategory?.value).isEqualTo(expenseCategory[1].first)
    }

    @Test
    fun `should return empty most expensive category for last year`() {
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(incomeOperation)

        val mostExpensiveCategory = bankOperationStatisticController.getMostExpensiveCategoryForLastYear()
        assertThat(mostExpensiveCategory).isNull()
    }

    @Test
    fun `should return most usable category for last year`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[1])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(100))
        }.build()
        val expenseOperation3 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(expenseOperation3)
        bankOperationController.create(incomeOperation)

        val mostExpensiveCategory = bankOperationStatisticController.getMostUsableCategoryForLastYear()
        assertThat(mostExpensiveCategory?.value).isEqualTo(expenseCategory[0].first)
    }

    @Test
    fun `should return empty most usable category for last year`() {
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.now().minusDays(1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(incomeOperation)

        val mostExpensiveCategory = bankOperationStatisticController.getMostUsableCategoryForLastYear()
        assertThat(mostExpensiveCategory).isNull()
    }

    @Test
    fun `should return correct total statistic by type`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(1000))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[1])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 4, 1))
            cost(BigDecimal(100))
        }.build()
        val expenseOperation3 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 4, 1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 3, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(expenseOperation3)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getTotalStatisticByType(
            BankOperationStatisticByTypeFilter(
                startDate = LocalDate.of(2000, 1, 1),
                endDate = LocalDate.of(2001, 1, 1),
            )
        )

        assertThat(statistic.months.size).isEqualTo(4)
        assertThat(statistic.series[OperationType.EXPENSE])
            .isEqualTo(
                listOf(
                    BigDecimal(1000).setScale(3),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal(200).setScale(3),
                )
            )
        assertThat(statistic.series[OperationType.INCOME])
            .isEqualTo(listOf(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal(10000).setScale(3), BigDecimal.ZERO))
    }

    @Test
    fun `should return empty total statistic by type`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(1000))
        }.build()

        bankOperationController.create(expenseOperation1)

        val statistic = bankOperationStatisticController.getTotalStatisticByType(
            BankOperationStatisticByTypeFilter(
                startDate = LocalDate.of(2001, 1, 1),
                endDate = LocalDate.of(2002, 1, 1),
            )
        )

        assertThat(statistic.months.size).isEqualTo(0)
        assertThat(statistic.series.size).isEqualTo(0)
    }

    @Test
    fun `should return correct monthly statistic by category`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(1000))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[1])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(100))
        }.build()
        val expenseOperation3 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(100))
        }.build()
        val expenseOperation4 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 2, 1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(expenseOperation3)
        bankOperationController.create(expenseOperation4)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getMonthlyExpenseStatisticByCategory(
            MonthlyExpenseStatisticByCategoryFilter(1, 2000)
        )

        assertThat(statistic.labels).isEqualTo(listOf(expenseCategory[0].second, expenseCategory[1].second))
        assertThat(statistic.series).isEqualTo(listOf(BigDecimal(1100).setScale(3), BigDecimal(100).setScale(3)))
        assertThat(statistic.other).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `should return correct other field for monthly statistic by category`() {
        repeat(20) {
            val idx = it % 11
            val expenseOperation = bankOperation {
                bankAccount(defaultBankAccount)
                operationCategory(expenseCategory[idx])
                type(OperationType.EXPENSE)
                dateCreated(LocalDate.of(2000, 1, it + 1))
                cost(BigDecimal((it + 1) * 100))
            }.build()
            bankOperationController.create(expenseOperation)
        }
        val expenseOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 2, 1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getMonthlyExpenseStatisticByCategory(
            MonthlyExpenseStatisticByCategoryFilter(1, 2000)
        )

        assertThat(statistic.labels.size).isEqualTo(9)
        assertThat(statistic.series.size).isEqualTo(9)
        assertThat(statistic.other).isEqualTo(BigDecimal(2100).setScale(3))
    }

    @Test
    fun `should return empty monthly statistic by category`() {
        val expenseOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 2, 1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getMonthlyExpenseStatisticByCategory(
            MonthlyExpenseStatisticByCategoryFilter(1, 2000)
        )

        assertThat(statistic.labels.size).isEqualTo(0)
        assertThat(statistic.series.size).isEqualTo(0)
        assertThat(statistic.other).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `should return correct statistic by category`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(1000))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[1])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 4, 1))
            cost(BigDecimal(100))
        }.build()
        val expenseOperation3 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 4, 1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 3, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(expenseOperation3)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getExpenseStatisticByCategory(
            ExpenseStatisticByCategoryFilter(
                startDate = LocalDate.of(2000, 1, 1),
                endDate = LocalDate.of(2001, 1, 1),
                categoryId = expenseCategory[0].first,
            )
        )

        assertThat(statistic.labels.size).isEqualTo(4)
        assertThat(statistic.series)
            .isEqualTo(
                listOf(
                    BigDecimal(1000).setScale(3),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal(100).setScale(3),
                )
            )
    }

    @Test
    fun `should return empty statistic by category`() {
        val expenseOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 2, 1))
            cost(BigDecimal(100))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 2, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getExpenseStatisticByCategory(
            ExpenseStatisticByCategoryFilter(
                startDate = LocalDate.of(2001, 1, 1),
                endDate = LocalDate.of(2002, 1, 1),
                categoryId = expenseCategory[0].first,
            )
        )

        assertThat(statistic.labels.size).isEqualTo(0)
        assertThat(statistic.series.size).isEqualTo(0)
    }

    @Test
    fun `should return median statistic by category`() {
        val expenseOperation1 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10))
        }.build()
        val expenseOperation2 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 15))
            cost(BigDecimal(20))
        }.build()
        val expenseOperation3 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 3, 2))
            cost(BigDecimal(20))
        }.build()
        val expenseOperation4 = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[1])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation1)
        bankOperationController.create(expenseOperation2)
        bankOperationController.create(expenseOperation3)
        bankOperationController.create(expenseOperation4)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getMedianStatisticByCategory(
            MedianStatisticByCategoryFilter(
                startDate = LocalDate.of(2000, 1, 1),
                endDate = LocalDate.of(2001, 1, 1),
                categoryId = expenseCategory[0].first,
            )
        )

        assertThat(statistic.size).isEqualTo(3)
        assertThat(statistic[0].month).isEqualTo(LocalDate.of(2000, 1, 1))
        assertThat(statistic[0].series).isEqualTo(
            listOf(
                BigDecimal(10).setScale(3),
                BigDecimal(15).setScale(3),
                BigDecimal(20).setScale(3),
            )
        )
        assertThat(statistic[1].month).isEqualTo(LocalDate.of(2000, 2, 1))
        assertThat(statistic[1].series).isEqualTo(listOf(
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
        ))
        assertThat(statistic[2].month).isEqualTo(LocalDate.of(2000, 3, 1))
        assertThat(statistic[2].series).isEqualTo(listOf(
            BigDecimal(20).setScale(3),
            BigDecimal(20).setScale(3),
            BigDecimal(20).setScale(3),
        ))
    }

    @Test
    fun `should return empty median statistic by category`() {
        val expenseOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(expenseCategory[0])
            type(OperationType.EXPENSE)
            dateCreated(LocalDate.of(1999, 1, 1))
            cost(BigDecimal(10))
        }.build()
        val incomeOperation = bankOperation {
            bankAccount(defaultBankAccount)
            operationCategory(incomeCategory)
            type(OperationType.INCOME)
            dateCreated(LocalDate.of(2000, 1, 1))
            cost(BigDecimal(10000))
        }.build()
        bankOperationController.create(expenseOperation)
        bankOperationController.create(incomeOperation)

        val statistic = bankOperationStatisticController.getMedianStatisticByCategory(
            MedianStatisticByCategoryFilter(
                startDate = LocalDate.of(2000, 1, 1),
                endDate = LocalDate.of(2001, 1, 1),
                categoryId = expenseCategory[0].first,
            )
        )

        assertThat(statistic.size).isEqualTo(0)
    }
}
