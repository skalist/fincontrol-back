package com.fincontrol.filter

import com.fincontrol.model.BankOperation
import com.fincontrol.specification.BankOperationSpecification
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.*
import java.time.LocalDate
import java.util.UUID

data class BankOperationStatisticByTypeFilter(
    val startDate: LocalDate?,
    val endDate: LocalDate?,
) {
    fun getSpecification(userId: UUID): Specification<BankOperation> {
        return where(BankOperationSpecification.userIdEqual(userId))
            .and(BankOperationSpecification.createdDateBetween(startDate, endDate))
    }
}
