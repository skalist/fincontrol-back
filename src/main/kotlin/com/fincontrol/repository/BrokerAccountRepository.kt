package com.fincontrol.repository

import com.fincontrol.model.BrokerAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for broker account entity
 */
interface BrokerAccountRepository: JpaRepository<BrokerAccount, UUID> {
    fun findAllByUserId(id: UUID): List<BrokerAccount>
}
