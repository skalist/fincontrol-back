package com.fincontrol.service

import com.fincontrol.dto.BankOperationStatisticByCategoryDto
import com.fincontrol.dto.BankOperationStatisticByTypeDto
import com.fincontrol.dto.LastMonthOfBankOperation
import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.filter.ExpenseValueStatisticByCategoryFilter
import com.fincontrol.model.*
import com.fincontrol.specification.BankOperationSpecification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.EntityManager

@Service
@Transactional(readOnly = true)
class BankOperationStatisticService(
    private val entityManager: EntityManager,
    private val authenticationFacade: AuthenticationFacade,
) {
    companion object {
        const val MAX_NUMBER_DISPLAYING_CATEGORIES = 9
    }

    fun getBankOperationStatisticByType(filter: BankOperationStatisticByTypeFilter): BankOperationStatisticByTypeDto {
        val result = getGroupedBankOperations(filter)
        val resultDates = result.map { LocalDate.of(it.year, it.month, 1) }.sorted()
        val labels = mutableListOf<LocalDate>()
        var iterableMonth = resultDates.first()
        while (iterableMonth.isBefore(resultDates.last().plusMonths(1))) {
            labels += iterableMonth
            iterableMonth = iterableMonth.plusMonths(1)
        }

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

        return entityManager.createQuery(query).singleResult.let {
            if (it == null) {
                return@let null
            }
            LastMonthOfBankOperation(it.monthValue, it.year)
        }
    }
}
