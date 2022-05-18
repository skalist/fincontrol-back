package com.fincontrol.dsl

import com.fincontrol.dto.AutocompleteOption
import com.fincontrol.dto.BankAccountUpsertDto
import com.fincontrol.dto.BankOperationUpsertDto
import com.fincontrol.dto.OperationCategoryUpsertDto
import com.fincontrol.model.OperationType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class OperationCategoryBuilder {
    private lateinit var name: String

    fun build() = OperationCategoryUpsertDto(null, name)

    fun name(name: String) {
        this.name = name
    }
}

fun operationCategory(init: OperationCategoryBuilder.() -> Unit) = OperationCategoryBuilder().apply(init)

class BankAccountBuilder {
    private lateinit var name: String

    fun build() = BankAccountUpsertDto(null, name)

    fun name(name: String) {
        this.name = name
    }
}

fun bankAccount(init: BankAccountBuilder.() -> Unit) = BankAccountBuilder().apply(init)

class BankOperationBuilder {
    private lateinit var operationCategory: AutocompleteOption<UUID>
    private lateinit var bankAccount: AutocompleteOption<UUID>
    private lateinit var type: OperationType
    private lateinit var dateCreated: LocalDate
    private lateinit var cost: BigDecimal

    fun build() = BankOperationUpsertDto(
        id = null,
        operationCategory = operationCategory,
        bankAccount = bankAccount,
        type = type,
        dateCreated = dateCreated,
        cost = cost,
    )

    fun operationCategory(operationCategory: Pair<UUID, String>) {
        this.operationCategory = AutocompleteOption(operationCategory.first, operationCategory.second)
    }

    fun bankAccount(bankAccount: Pair<UUID, String>) {
        this.bankAccount = AutocompleteOption(bankAccount.first, bankAccount.second)
    }

    fun type(type: OperationType) {
        this.type = type
    }

    fun dateCreated(dateCreated: LocalDate) {
        this.dateCreated = dateCreated
    }

    fun cost(cost: BigDecimal) {
        this.cost = cost
    }
}

fun bankOperation(init: BankOperationBuilder.() -> Unit) = BankOperationBuilder().apply(init)
