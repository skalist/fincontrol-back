package com.fincontrol.model

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Model for investment calculator
 */
@Entity
data class InvestmentCalculator(
    /**
     * Identifier
     */
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    /**
     * User identifier
     */
    @Type(type = "uuid-char")
    val userId: UUID,
    /**
     * Age when you are going to start invest
     */
    val startAge: Int,
    /**
     * Age when you are going to get retired
     */
    val retiredAge: Int,
    /**
     * Average investment return per year
     */
    val investmentReturnPercent: BigDecimal,
    /**
     * Average inflation rate per year
     */
    val inflationPercent: BigDecimal,
    /**
     * Comfortable salary now per month
     */
    val expectedSalaryNowPerMonth: BigDecimal,
    /**
     * Current cost of all broker accounts
     */
    val currentAccountsCost: BigDecimal,
)
