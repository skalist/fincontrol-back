package com.fincontrol.service

import com.fincontrol.dto.*
import com.fincontrol.filter.AnnualStatisticByCategoryFilter
import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseValueStatisticByCategoryFilter
import com.fincontrol.filter.MedianStatisticByCategoryFilter
import com.fincontrol.model.*
import com.fincontrol.repository.BankOperationRepository
import com.fincontrol.specification.BankOperationSpecification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager

@Service
@Transactional(readOnly = true)
class BankOperationStatisticService(
    private val entityManager: EntityManager,
    private val authenticationFacade: AuthenticationFacade,
    private val bankOperationRepository: BankOperationRepository,
) {
    companion object {
        const val MAX_NUMBER_DISPLAYING_CATEGORIES = 9
    }

    fun getBankOperationStatisticByType(filter: BankOperationStatisticByTypeFilter): BankOperationStatisticByTypeDto {
        val result = getGroupedBankOperations(filter)
        val labels = getLabelsForAnnualStatistic(
            availaibleMonths = result.map { LocalDate.of(it.year, it.month, 1) },
        )

        val series = result.groupBy { it.type }.map { (type, rows) ->
            val monthValues = MutableList<BigDecimal>(labels.size) { BigDecimal.ZERO }
            rows.forEach { row ->
                val rowDate = LocalDate.of(row.year, row.month, 1)
                val index = labels.indexOf(rowDate)
                monthValues[index] = row.value
            }
            type to monthValues
        }.toMap()

        return BankOperationStatisticByTypeDto(labels, series)
    }

    private fun getGroupedBankOperations(filter: BankOperationStatisticByTypeFilter): List<BankOperationStatisticByType> {
        val userId = authenticationFacade.getUserId()
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(BankOperationStatisticByType::class.java)
        val root = query.from(BankOperation::class.java)
        val monthOfYear = builder.function("month", Int::class.java, root[BankOperation_.dateCreated])
        val year = builder.function("year", Int::class.java, root[BankOperation_.dateCreated])

        query.multiselect(root[BankOperation_.type], monthOfYear, year, builder.sum(root[BankOperation_.cost]))
        query.where(filter.getSpecification(userId).toPredicate(root, query, builder))
        query.groupBy(root[BankOperation_.type], monthOfYear, year)

        return entityManager.createQuery(query).resultList
    }

    fun getBankOperationStatisticByCategory(
        filter: ExpenseValueStatisticByCategoryFilter,
    ): BankOperationStatisticByCategoryDto {
        val result = getGroupedBankOperationByCategory(filter)

        val labels = mutableListOf<String>()
        val series = mutableListOf<BigDecimal>()
        var other = BigDecimal.ZERO

        result.sortedByDescending { it.value }.forEachIndexed { index, statistic ->
            if (index < MAX_NUMBER_DISPLAYING_CATEGORIES) {
                labels += statistic.categoryName
                series += statistic.value
            } else {
                other = other.plus(statistic.value)
            }
        }

        return BankOperationStatisticByCategoryDto(labels, series, other)
    }

    private fun getGroupedBankOperationByCategory(
        filter: ExpenseValueStatisticByCategoryFilter
    ): List<BankOperationStatisticByCategory> {
        val userId = authenticationFacade.getUserId()

        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(BankOperationStatisticByCategory::class.java)
        val root = query.from(BankOperation::class.java)
        query.multiselect(
            root[BankOperation_.operationCategory][OperationCategory_.name],
            builder.sum(root[BankOperation_.cost])
        )
        query.where(filter.getSpecification(userId).toPredicate(root, query, builder))
        query.groupBy(root[BankOperation_.operationCategory][OperationCategory_.name])

        return entityManager.createQuery(query).resultList
    }

    fun getLastFilledMonth(): LastMonthOfBankOperation? {
        val userId = authenticationFacade.getUserId()

        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(LocalDate::class.java)
        val root = query.from(BankOperation::class.java)
        val specification = where(BankOperationSpecification.userIdEqual(userId))
            .and(BankOperationSpecification.typeEqual(OperationType.EXPENSE))

        query.where(specification.toPredicate(root, query, builder))
        query.select(builder.greatest(root[BankOperation_.dateCreated]))

        return entityManager.createQuery(query).singleResult?.let {
            LastMonthOfBankOperation(it.monthValue, it.year)
        }
    }

    fun getAnnualStatisticByCategory(filter: AnnualStatisticByCategoryFilter): AnnualBankOperationStatisticByCategoryDto {
        val result = getAnnualGroupedBankOperationStatisticByCategory(filter)
        val labels = getLabelsForAnnualStatistic(
            availaibleMonths = result.map { LocalDate.of(it.year, it.month, 1) },
        )

        val series = MutableList<BigDecimal>(labels.size) { BigDecimal.ZERO }
        result.forEach { row ->
            val rowDate = LocalDate.of(row.year, row.month, 1)
            val index = labels.indexOf(rowDate)
            series[index] = row.value
        }

        return AnnualBankOperationStatisticByCategoryDto(labels, series)
    }

    fun getLabelsForAnnualStatistic(availaibleMonths: List<LocalDate>): List<LocalDate> {
        val sortedMonths = availaibleMonths.sorted()
        if (sortedMonths.isEmpty()) {
            return emptyList()
        }
        val labels = mutableListOf<LocalDate>()
        var iterableMonth = sortedMonths.first()
        while (iterableMonth.isBefore(sortedMonths.last().plusMonths(1))) {
            labels += iterableMonth
            iterableMonth = iterableMonth.plusMonths(1)
        }

        return labels
    }

    private fun getAnnualGroupedBankOperationStatisticByCategory(
        filter: AnnualStatisticByCategoryFilter
    ): List<AnnualBankOperationStatisticByCategory> {
        val userId = authenticationFacade.getUserId()
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(AnnualBankOperationStatisticByCategory::class.java)
        val root = query.from(BankOperation::class.java)
        val monthOfYear = builder.function("month", Int::class.java, root[BankOperation_.dateCreated])
        val year = builder.function("year", Int::class.java, root[BankOperation_.dateCreated])

        query.multiselect(monthOfYear, year, builder.sum(root[BankOperation_.cost]))
        query.where(filter.getSpecification(userId).toPredicate(root, query, builder))
        query.groupBy(monthOfYear, year)

        return entityManager.createQuery(query).resultList
    }

    fun getMostExpensiveCategoryForLastYear(): AutocompleteOption<UUID>? {
        val userId = authenticationFacade.getUserId()
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(CostByCategory::class.java)
        val root = query.from(BankOperation::class.java)
        query.multiselect(root[BankOperation_.operationCategory], builder.sum(root[BankOperation_.cost]))
        val specification =
            where(BankOperationSpecification.createdDateBetween(LocalDate.now().minusYears(1), LocalDate.now()))
                .and(BankOperationSpecification.typeEqual(OperationType.EXPENSE))
                .and(BankOperationSpecification.userIdEqual(userId))
        query.where(specification.toPredicate(root, query, builder))
        query.groupBy(root[BankOperation_.operationCategory])

        val resultList = entityManager.createQuery(query).resultList
        return resultList.maxByOrNull { it.sumCosts }?.category?.let { AutocompleteOption(it.id, it.name) }
    }

    fun getMostUsableCategoryForLastYear(): AutocompleteOption<UUID>? {
        val userId = authenticationFacade.getUserId()
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(UsageByCategory::class.java)
        val root = query.from(BankOperation::class.java)
        query.multiselect(root[BankOperation_.operationCategory], builder.count(root))
        val specification =
            where(BankOperationSpecification.createdDateBetween(LocalDate.now().minusYears(1), LocalDate.now()))
                .and(BankOperationSpecification.userIdEqual(userId))
                .and(BankOperationSpecification.typeEqual(OperationType.EXPENSE))
        query.where(specification.toPredicate(root, query, builder))
        query.groupBy(root[BankOperation_.operationCategory])

        val resultList = entityManager.createQuery(query).resultList
        return resultList.maxByOrNull { it.countUsage }?.category?.let { AutocompleteOption(it.id, it.name) }
    }

    fun getMedianStatisticByCategory(filter: MedianStatisticByCategoryFilter): List<MedianBankOperationStatisticByCategoryDto> {
        val userId = authenticationFacade.getUserId()
        val bankOperations = bankOperationRepository.findAll(filter.getSpecification(userId))
        val bankOperationMap = bankOperations.groupBy { it.dateCreated.withDayOfMonth(1) }
        return bankOperationMap.entries.map { (month, values) ->
            val costValues = values.map { it.cost }.sorted()
            val averageValue = costValues.sumOf { it }.div(BigDecimal(costValues.size))
            MedianBankOperationStatisticByCategoryDto(
                month,
                listOf(costValues.minOf { it }, averageValue, costValues.maxOf { it })
            )
        }
    }
}
