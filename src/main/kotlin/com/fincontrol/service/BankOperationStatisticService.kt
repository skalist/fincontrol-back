package com.fincontrol.service

import com.fincontrol.dto.BankOperationStatisticByTypeDto
import com.fincontrol.filter.BankOperationStatisticByTypeFilter
import com.fincontrol.model.BankOperation
import com.fincontrol.model.BankOperation_
import com.fincontrol.model.BankOperationStatisticByType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import javax.persistence.EntityManager

@Service
@Transactional(readOnly = true)
class BankOperationStatisticService(
    private val entityManager: EntityManager,
    private val authenticationFacade: AuthenticationFacade,
) {
    fun getBankOperationStatisticByType(filter: BankOperationStatisticByTypeFilter): BankOperationStatisticByTypeDto {
        val result = getGroupedBankOperations(filter)
        val months = result.map { it.month }.distinct().sorted()
        val series = result.groupBy { it.type }.map { (type, rows) ->
            val monthValues = MutableList<BigDecimal>(months.size) { BigDecimal.ZERO }
            rows.forEach { row ->
                val index = months.indexOf(row.month)
                monthValues[index] = row.value
            }
            type to monthValues
        }.toMap()

        return BankOperationStatisticByTypeDto(months, series)
    }

    private fun getGroupedBankOperations(filter: BankOperationStatisticByTypeFilter): List<BankOperationStatisticByType> {
        val userId = authenticationFacade.getUserId()
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(BankOperationStatisticByType::class.java)
        val root = query.from(BankOperation::class.java)
        val monthOfYear = builder.function("month", Int::class.java, root[BankOperation_.dateCreated])

        query.multiselect(root[BankOperation_.type], monthOfYear, builder.sum(root[BankOperation_.cost]))
        query.where(filter.getSpecification(userId).toPredicate(root, query, builder))
        query.groupBy(root[BankOperation_.type], monthOfYear)

        return entityManager.createQuery(query).resultList
    }
}
