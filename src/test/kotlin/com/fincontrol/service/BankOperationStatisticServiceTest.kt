package com.fincontrol.service

import com.fincontrol.filter.AnnualStatisticByCategoryFilter
import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseValueStatisticByCategoryFilter
import com.fincontrol.model.AnnualBankOperationStatisticByCategory
import com.fincontrol.model.BankOperationStatisticByCategory
import com.fincontrol.model.BankOperationStatisticByType
import com.fincontrol.model.OperationType
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
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
    private val fakeBankOperationByCategoryQuery =
        mockk<CriteriaQuery<BankOperationStatisticByCategory>>(relaxed = true)
    private val fakeAnnualBankOperationByCategoryQuery =
        mockk<CriteriaQuery<AnnualBankOperationStatisticByCategory>>(relaxed = true)

    private val bankOperationStatisticService = BankOperationStatisticService(entityManager, authenticationFacade)

    @Test
    fun `should return correct statistic by type`() {
        val values = listOf(
            BankOperationStatisticByType(OperationType.EXPENSE, 1, 2000, BigDecimal(100)),
            BankOperationStatisticByType(OperationType.INCOME, 2, 2000, BigDecimal(200)),
            BankOperationStatisticByType(OperationType.EXPENSE, 4, 2000, BigDecimal(10)),
        )
        every {
            entityManager.criteriaBuilder.createQuery(BankOperationStatisticByType::class.java)
        } returns fakeBankOperationByTypeQuery
        every { entityManager.createQuery(fakeBankOperationByTypeQuery).resultList } returns values
        every { authenticationFacade.getUserId() } returns UUID.randomUUID()

        val statistic = bankOperationStatisticService.getBankOperationStatisticByType(
            BankOperationStatisticByTypeFilter(any(), any())
        )

        assertThat(statistic.months.size).isEqualTo(4)
        assertThat(statistic.series[OperationType.EXPENSE])
            .isEqualTo(listOf(BigDecimal(100), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal(10)))
        assertThat(statistic.series[OperationType.INCOME])
            .isEqualTo(listOf(BigDecimal.ZERO, BigDecimal(200), BigDecimal.ZERO, BigDecimal.ZERO))
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

        assertThat(statistic.labels).isEqualTo(listOf("Category2", "Category3", "Category1"))
        assertThat(statistic.series).isEqualTo(listOf(BigDecimal(30), BigDecimal(20), BigDecimal(10)))
        assertThat(statistic.other).isEqualTo(BigDecimal.ZERO)
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

        assertThat(statistic.labels.size).isEqualTo(9)
        assertThat(statistic.series.size).isEqualTo(9)
        assertThat(statistic.other).isEqualTo(BigDecimal(550))
    }

    @Test
    fun `should return correct annual statistic by category`() {
        val values = listOf(
            AnnualBankOperationStatisticByCategory(1, 2000, BigDecimal(100)),
            AnnualBankOperationStatisticByCategory(3, 2000, BigDecimal(150)),
            AnnualBankOperationStatisticByCategory(4, 2000, BigDecimal(300)),
        )
        every {
            entityManager.criteriaBuilder.createQuery(AnnualBankOperationStatisticByCategory::class.java)
        } returns fakeAnnualBankOperationByCategoryQuery
        every { entityManager.createQuery(fakeAnnualBankOperationByCategoryQuery).resultList } returns values
        every { authenticationFacade.getUserId() } returns UUID.randomUUID()

        val statistic = bankOperationStatisticService.getAnnualStatisticByCategory(
            AnnualStatisticByCategoryFilter(any(), any(), UUID.randomUUID())
        )

        assertThat(statistic.labels.size).isEqualTo(4)
        assertThat(statistic.series)
            .isEqualTo(listOf(BigDecimal(100), BigDecimal.ZERO, BigDecimal(150), BigDecimal(300)))
    }
}
