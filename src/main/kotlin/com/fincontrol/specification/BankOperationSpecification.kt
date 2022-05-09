package com.fincontrol.specification

import com.fincontrol.model.*
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.util.UUID
import javax.persistence.criteria.Predicate

object BankOperationSpecification {
    fun userIdEqual(userId: UUID): Specification<BankOperation> {
        return Specification<BankOperation> { root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.userId], userId)
        }
    }

    fun createdDateBetween(startDate: LocalDate?, endDate: LocalDate?): Specification<BankOperation> {
        return Specification<BankOperation> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root[BankOperation_.dateCreated], startDate))
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root[BankOperation_.dateCreated], endDate))
            }
            return@Specification cb.and(*predicates.toTypedArray())
        }
    }

    fun typeEqual(type: OperationType): Specification<BankOperation> {
        return Specification<BankOperation> { root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.type], type)
        }
    }

    fun categoryIdEqual(categoryId: UUID?): Specification<BankOperation>? {
        if (categoryId == null) {
            return null
        }
        return Specification<BankOperation> { root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.operationCategory][OperationCategory_.id], categoryId)
        }
    }
    
    fun bankAccountIdEqual(bankAccountId: UUID?): Specification<BankOperation>? {
        if (bankAccountId == null) {
            return null
        }
        return Specification<BankOperation> {root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.bankAccount][BankAccount_.id], bankAccountId)
        }
    }
}
