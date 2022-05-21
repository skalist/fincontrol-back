package com.fincontrol.repository

import com.fincontrol.model.InvestmentCalculator
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InvestmentCalculatorRepository: JpaRepository<InvestmentCalculator, UUID> {
    fun findByUserId(userId: UUID): InvestmentCalculator?
}
