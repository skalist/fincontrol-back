package com.fincontrol.repository

import com.fincontrol.model.BrokerAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BrokerAccountRepository: JpaRepository<BrokerAccount, UUID> {
    fun findAllByUserId(id: UUID): List<BrokerAccount>
}
