package com.fincontrol.filter

import com.fincontrol.model.BankOperation
import com.fincontrol.model.OperationType
import com.fincontrol.specification.BankOperationSpecification
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*

data class BankOperationStatisticByTypeFilter(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate?,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate?,
) {
    fun getSpecification(userId: UUID): Specification<BankOperation> {
        return where(BankOperationSpecification.userIdEqual(userId))
            .and(BankOperationSpecification.createdDateBetween(startDate, endDate))
    }
}

data class ExpenseValueStatisticByCategoryFilter(
    val month: Int,
    val year: Int,
) {
    fun getSpecification(userId: UUID): Specification<BankOperation> {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1)

        return where(BankOperationSpecification.userIdEqual(userId))
            .and(BankOperationSpecification.createdDateBetween(startDate, endDate))
            .and(BankOperationSpecification.typeEqual(OperationType.EXPENSE))
    }
}

data class AnnualStatisticByCategoryFilter(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate?,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate?,
    val categoryId: UUID,
) {
    fun getSpecification(userId: UUID): Specification<BankOperation> {
        return where(BankOperationSpecification.userIdEqual(userId))
            .and(BankOperationSpecification.createdDateBetween(startDate, endDate))
            .and(BankOperationSpecification.typeEqual(OperationType.EXPENSE))
            .and(BankOperationSpecification.categoryIdEqual(categoryId))
    }
}
