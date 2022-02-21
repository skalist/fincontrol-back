package com.fincontrol.repository

import com.fincontrol.model.BankAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BankAccountRepository: JpaRepository<BankAccount, UUID> {
    fun findAllByUserId(userId: UUID): List<BankAccount>
}
