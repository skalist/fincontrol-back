package com.fincontrol.repository

import com.fincontrol.model.OperationCategory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OperationCategoryRepository: JpaRepository<OperationCategory, UUID> {
    fun findAllByUserId(userId: UUID): List<OperationCategory>
}
