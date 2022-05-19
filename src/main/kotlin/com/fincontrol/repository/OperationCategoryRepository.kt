package com.fincontrol.repository

import com.fincontrol.model.OperationCategory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for operation category entity
 */
interface OperationCategoryRepository: JpaRepository<OperationCategory, UUID> {
    fun findAllByUserIdOrderByName(userId: UUID): List<OperationCategory>
}
