package com.fincontrol.dto

import java.math.BigDecimal

/**
 * Request dto for getting investment info
 */
data class InvestmentCalculatorRequest(
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
)

/**
 * Response dto for getting investment info
 */
data class InvestmentCalculatorResponse(
    /**
     * Desired salary in retired age
     */
    val retiredSalaryPerMonth: BigDecimal,
    /**
     * Total cost of broker account in retired age
     */
    val accumulatedInvestments: BigDecimal,
    /**
     * Investment amount which you need insert into account per month
     */
    val investmentPerMonth: BigDecimal,
)
