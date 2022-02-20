package com.fincontrol.repository

import com.fincontrol.model.ExpenseType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExpenseTypeRepository: JpaRepository<ExpenseType, UUID>
