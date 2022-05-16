package com.fincontrol.specification

import com.fincontrol.model.*
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.util.*
import javax.persistence.criteria.Predicate

/**
 * Specification for bank operation entity
 */
object BankOperationSpecification {
    /**
     * User id equal condition
     * @param userId user identifier
     * @return specification
      */
    fun userIdEqual(userId: UUID): Specification<BankOperation> {
        return Specification<BankOperation> { root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.userId], userId)
        }
    }

    /**
     * Condition for dateCreated where value between 2 dates
     * @param startDate min value of date
     * @param endDate max value of date
     * @return specification
     */
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

    /**
     * Type equal condition
     * @param type type of operation
     * @return specification
     */
    fun typeEqual(type: OperationType): Specification<BankOperation> {
        return Specification<BankOperation> { root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.type], type)
        }
    }

    /**
     * Category id equal condition
     * @param categoryId category identifier
     * @return specification
     */
    fun categoryIdEqual(categoryId: UUID?): Specification<BankOperation>? {
        if (categoryId == null) {
            return null
        }
        return Specification<BankOperation> { root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.operationCategory][OperationCategory_.id], categoryId)
        }
    }

    /**
     * Bank account id equal condition
     * @param bankAccountId bank account identifier
     * @return specification
     */
    fun bankAccountIdEqual(bankAccountId: UUID?): Specification<BankOperation>? {
        if (bankAccountId == null) {
            return null
        }
        return Specification<BankOperation> {root, _, cb ->
            return@Specification cb.equal(root[BankOperation_.bankAccount][BankAccount_.id], bankAccountId)
        }
    }
}
