package com.fincontrol.filter

import com.fincontrol.model.BankOperation
import com.fincontrol.specification.BankOperationSpecification
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*

/**
 * Filter for getting bank operation entities for registry
 */
data class BankOperationFilter(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate?,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate?,
    val categoryId: UUID?,
    val bankAccountId: UUID?,
) {
    fun getSpecification(userId: UUID): Specification<BankOperation> {
        return where(BankOperationSpecification.userIdEqual(userId))
            .and(BankOperationSpecification.createdDateBetween(startDate, endDate))
            .and(BankOperationSpecification.categoryIdEqual(categoryId))
            .and(BankOperationSpecification.bankAccountIdEqual(bankAccountId))
    }
}
