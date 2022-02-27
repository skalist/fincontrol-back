package com.fincontrol.model

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
data class BankOperation(
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    @Type(type = "uuid-char")
    val userId: UUID,
    @ManyToOne
    @JoinColumn(name = "operation_category_id")
    val operationCategory: OperationCategory,
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    val bankAccount: BankAccount,
    @Enumerated(EnumType.STRING)
    val type: OperationType,
    val dateCreated: LocalDate,
    val cost: BigDecimal,
)
