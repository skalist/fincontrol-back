package com.fincontrol.repository

import com.fincontrol.model.Industry
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IndustryRepository: JpaRepository<Industry, UUID> {
    fun findAllByUserId(userId: UUID): List<Industry>
}
