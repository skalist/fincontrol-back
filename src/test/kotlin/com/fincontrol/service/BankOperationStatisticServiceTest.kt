package com.fincontrol.service

import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseValueStatisticByCategoryFilter
import com.fincontrol.model.BankOperationStatisticByCategory
import com.fincontrol.model.BankOperationStatisticByType
import com.fincontrol.model.OperationType
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import java.math.BigDecimal
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaQuery

@ExtendWith(MockKExtension::class)
internal class BankOperationStatisticServiceTest {
    private val entityManager = mockk<EntityManager>(relaxed = true)
    private val authenticationFacade = mockk<AuthenticationFacade>()
    private val fakeBankOperationByTypeQuery = mockk<CriteriaQuery<BankOperationStatisticByType>>(relaxed = true)
    private val fakeBankOperationByCategoryQuery = mockk<CriteriaQuery<BankOperationStatisticByCategory>>(relaxed = true)

    private val bankOperationStatisticService = BankOperationStatisticService(entityManager, authenticationFacade)

    @Test
    fun `should return correct statistic by type`() {
        val values = listOf(
            BankOperationStatisticByType(OperationType.EXPENSE, 1, BigDecimal(100)),
            BankOperationStatisticByType(OperationType.INCOME, 2, BigDecimal(200)),
            BankOperationStatisticByType(OperationType.EXPENSE, 3, BigDecimal(10)),
        )
        every {
            entityManager.criteriaBuilder.createQuery(BankOperationStatisticByType::class.java)
        } returns fakeBankOperationByTypeQuery
        every { entityManager.createQuery(fakeBankOperationByTypeQuery).resultList } returns values
        every { authenticationFacade.getUserId() } returns UUID.randomUUID()

        val statistic = bankOperationStatisticService.getBankOperationStatisticByType(
            BankOperationStatisticByTypeFilter(any(), any())
        )

        Assertions.assertEquals(statistic.months.size, 3)
        Assertions.assertEquals(statistic.series[OperationType.EXPENSE]!!.size, 3)
        Assertions.assertEquals(statistic.series[OperationType.INCOME]!!.size, 3)
        Assertions.assertEquals(statistic.series[OperationType.EXPENSE]!![0], BigDecimal(100))
        Assertions.assertEquals(statistic.series[OperationType.EXPENSE]!![1], BigDecimal(0))
        Assertions.assertEquals(statistic.series[OperationType.EXPENSE]!![2], BigDecimal(10))
        Assertions.assertEquals(statistic.series[OperationType.INCOME]!![0], BigDecimal(0))
        Assertions.assertEquals(statistic.series[OperationType.INCOME]!![1], BigDecimal(200))
        Assertions.assertEquals(statistic.series[OperationType.INCOME]!![2], BigDecimal(0))
    }

    @Test
    fun `should return correct statistic by category`() {
        val values = listOf(
            BankOperationStatisticByCategory("Category1", BigDecimal(10)),
            BankOperationStatisticByCategory("Category2", BigDecimal(30)),
            BankOperationStatisticByCategory("Category3", BigDecimal(20)),
        )
        every {
            entityManager.criteriaBuilder.createQuery(BankOperationStatisticByCategory::class.java)
        } returns fakeBankOperationByCategoryQuery
        every { entityManager.createQuery(fakeBankOperationByCategoryQuery).resultList } returns values
        every { authenticationFacade.getUserId() } returns UUID.randomUUID()

        val statistic = bankOperationStatisticService.getBankOperationStatisticByCategory(
            ExpenseValueStatisticByCategoryFilter(1, 2000)
        )

        Assertions.assertEquals(statistic.labels.size, 3)
        Assertions.assertEquals(statistic.series.size, 3)

        Assertions.assertEquals(statistic.labels[0], "Category2")
        Assertions.assertEquals(statistic.labels[1], "Category3")
        Assertions.assertEquals(statistic.labels[2], "Category1")
        Assertions.assertEquals(statistic.other, BigDecimal.ZERO)
    }

    @Test
    fun `should return correct other field for statistic by category`() {
        val values = mutableListOf<BankOperationStatisticByCategory>()
        repeat(20) {
            values += BankOperationStatisticByCategory("Category${it}", BigDecimal(it * 10))
        }
        every {
            entityManager.criteriaBuilder.createQuery(BankOperationStatisticByCategory::class.java)
        } returns fakeBankOperationByCategoryQuery
        every { entityManager.createQuery(fakeBankOperationByCategoryQuery).resultList } returns values
        every { authenticationFacade.getUserId() } returns UUID.randomUUID()

        val statistic = bankOperationStatisticService.getBankOperationStatisticByCategory(
            ExpenseValueStatisticByCategoryFilter(1, 2000)
        )

        Assertions.assertEquals(statistic.labels.size, 9)
        Assertions.assertEquals(statistic.series.size, 9)

        Assertions.assertEquals(statistic.other, BigDecimal(550))
    }
}
