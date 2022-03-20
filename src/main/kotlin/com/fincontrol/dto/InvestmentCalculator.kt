package com.fincontrol.dto

import java.math.BigDecimal

data class InvestmentCalculatorRequest(
    val startAge: Int,
    val retiredAge: Int,
    val investmentReturnPercent: BigDecimal,
    val inflationPercent: BigDecimal,
    val expectedSalaryNowPerMonth: BigDecimal,
)

data class InvestmentCalculatorResponse(
    val retiredSalaryPerMonth: BigDecimal,
    val accumulatedInvestments: BigDecimal,
    val investmentPerMonth: BigDecimal,
)
