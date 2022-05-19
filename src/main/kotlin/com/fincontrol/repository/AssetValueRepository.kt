package com.fincontrol.repository

import com.fincontrol.model.AssetValue
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for asset value entity
 */
interface AssetValueRepository: JpaRepository<AssetValue, UUID> {
    fun findAllByUserId(userId: UUID): List<AssetValue>
}
