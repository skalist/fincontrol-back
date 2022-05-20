package com.fincontrol.service

import com.fincontrol.dto.InvestmentCalculatorRequest
import com.fincontrol.dto.InvestmentCalculatorResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Service for working with investment calculator
 */
@Service
@Transactional(readOnly = true)
class InvestmentCalculatorService {
    /**
     * Method for getting info how much do we need invest for getting good live in retired age
     * @param investmentRequest info about inflation rate, investment return rate and desire salary
     * @return info about investing
     */
    fun calculate(@RequestBody investmentRequest: InvestmentCalculatorRequest): InvestmentCalculatorResponse {
        val timeLeft = investmentRequest.retiredAge - investmentRequest.startAge

        val investmentReturnShare = investmentRequest.investmentReturnPercent.divide(BigDecimal(100)) + BigDecimal.ONE
        val totalInvestmentReturn = (1..timeLeft).map { investmentReturnShare.pow(it) }.sumOf { it }

        val inflationShare = investmentRequest.inflationPercent.divide(BigDecimal(100)) + BigDecimal.ONE
        val totalInflation = inflationShare.pow(timeLeft)

        val retiredSalaryPerMonth = totalInflation.multiply(investmentRequest.expectedSalaryNowPerMonth)

        val investmentPerMonth =
            retiredSalaryPerMonth.divide(
                totalInvestmentReturn.multiply(investmentReturnShare - inflationShare),
                2,
                RoundingMode.HALF_EVEN
            )

        val accumulatedInvestments = investmentPerMonth.multiply(totalInvestmentReturn).multiply(BigDecimal(12))
        return InvestmentCalculatorResponse(
            retiredSalaryPerMonth = retiredSalaryPerMonth.setScale(2, RoundingMode.HALF_EVEN),
            accumulatedInvestments = accumulatedInvestments.setScale(2, RoundingMode.HALF_EVEN),
            investmentPerMonth = investmentPerMonth,
            totalYears = timeLeft,
        )
    }
}
