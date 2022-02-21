package com.fincontrol.repository

import com.fincontrol.model.BankOperation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BankOperationRepository: JpaRepository<BankOperation, UUID> {
    fun findAllByUserIdOrderByDateCreatedDesc(userId: UUID): List<BankOperation>
}
