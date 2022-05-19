package com.fincontrol.repository

import com.fincontrol.model.BankOperation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

/**
 * Repository for bank operation entity
 */
interface BankOperationRepository: JpaRepository<BankOperation, UUID>, JpaSpecificationExecutor<BankOperation> {
}
