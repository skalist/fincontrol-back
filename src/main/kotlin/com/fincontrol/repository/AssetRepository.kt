package com.fincontrol.repository

import com.fincontrol.model.Asset
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AssetRepository: JpaRepository<Asset, UUID> {
    fun findAllByUserId(userId: UUID): List<Asset>
}
