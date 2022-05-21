package com.fincontrol.service

import com.fincontrol.dto.InvestmentCalculatorDto
import com.fincontrol.dto.InvestmentCalculatorResultDto
import com.fincontrol.model.InvestmentCalculator
import com.fincontrol.repository.InvestmentCalculatorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 * Service for working with investment calculator
 */
@Service
@Transactional(readOnly = true)
class InvestmentCalculatorService(
    private val authenticationFacade: AuthenticationFacade,
    private val investmentCalculatorRepository: InvestmentCalculatorRepository,
) {
    /**
     * Method for getting info how much do we need invest for getting good live in retired age
     * @param investmentRequest info about inflation rate, investment return rate and desire salary
     * @return info about investing
     */
    fun calculate(@RequestBody investmentRequest: InvestmentCalculatorDto): InvestmentCalculatorResultDto {
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
        return InvestmentCalculatorResultDto(
            retiredSalaryPerMonth = retiredSalaryPerMonth.setScale(2, RoundingMode.HALF_EVEN),
            accumulatedInvestments = accumulatedInvestments.setScale(2, RoundingMode.HALF_EVEN),
            investmentPerMonth = investmentPerMonth,
            totalYears = timeLeft,
        )
    }

    /**
     * Getting entity of investment calculator for current user
     * @return dto of investment calculator
     */
    fun findOne(): InvestmentCalculatorDto? {
        val userId = authenticationFacade.getUserId()
        return investmentCalculatorRepository.findByUserId(userId)?.let {
            InvestmentCalculatorDto(
                startAge = it.startAge,
                retiredAge = it.retiredAge,
                investmentReturnPercent = it.investmentReturnPercent,
                inflationPercent = it.inflationPercent,
                expectedSalaryNowPerMonth = it.expectedSalaryNowPerMonth,
            )
        }
    }

    /**
     * Create or update investment calculator for current user
     * @param dto info about investment calculator
     */
    fun createOrUpdate(dto: InvestmentCalculatorDto) {
        val userId = authenticationFacade.getUserId()
        val investmentCalculator = investmentCalculatorRepository.findByUserId(userId)
        val newInvestmentCalculator =
            if (investmentCalculator == null) {
                create(dto, userId)
            } else {
                update(dto, investmentCalculator)
            }

        investmentCalculatorRepository.save(newInvestmentCalculator)
    }

    /**
     * Create new investment calculator for current user
     * @param dto info about investment calculator data
     * @param userId identifier of current user
     * @return investment calculator entity
     */
    private fun create(dto: InvestmentCalculatorDto, userId: UUID) =
        InvestmentCalculator(
            userId = userId,
            startAge = dto.startAge,
            retiredAge = dto.retiredAge,
            investmentReturnPercent = dto.investmentReturnPercent,
            inflationPercent = dto.inflationPercent,
            expectedSalaryNowPerMonth = dto.expectedSalaryNowPerMonth,
        )

    /**
     * Update existing investment calculator
     * @param dto info about investment calculator
     * @param investmentCalculator old investment calculator data
     * @return investment calculator entity
     */
    private fun update(dto: InvestmentCalculatorDto, investmentCalculator: InvestmentCalculator) =
        investmentCalculator.copy(
            startAge = dto.startAge,
            retiredAge = dto.retiredAge,
            investmentReturnPercent = dto.investmentReturnPercent,
            inflationPercent = dto.inflationPercent,
            expectedSalaryNowPerMonth = dto.expectedSalaryNowPerMonth,
        )
}
