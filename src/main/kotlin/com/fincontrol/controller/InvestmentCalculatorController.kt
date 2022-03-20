package com.fincontrol.controller

import com.fincontrol.dto.InvestmentCalculatorRequest
import com.fincontrol.dto.InvestmentCalculatorResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.RoundingMode

@RestController
@RequestMapping("investment-calculator")
class InvestmentCalculatorController {
    @PostMapping
    fun calculate(@RequestBody investmentRequest: InvestmentCalculatorRequest): InvestmentCalculatorResponse {
        val timeLeft = investmentRequest.retiredAge - investmentRequest.startAge

        val investmentReturnShare = investmentRequest.investmentReturnPercent.divide(BigDecimal(100)) + BigDecimal.ONE
        val totalInvestmentReturn = (1..timeLeft).map { investmentReturnShare.pow(it) }.sumOf { it }

        val inflationShare = investmentRequest.inflationPercent.divide(BigDecimal(100)) + BigDecimal.ONE
        val totalInflation = inflationShare.pow(timeLeft)

        val retiredSalaryPerMonth = totalInflation.multiply(investmentRequest.expectedSalaryNowPerMonth)

        val investmentPerMonth =
            retiredSalaryPerMonth.divide(totalInvestmentReturn.multiply(investmentReturnShare - inflationShare), 2, RoundingMode.HALF_EVEN)

        val accumulatedInvestments = investmentPerMonth.multiply(totalInvestmentReturn).multiply(BigDecimal(12))
        return InvestmentCalculatorResponse(retiredSalaryPerMonth, accumulatedInvestments, investmentPerMonth)
    }
}
