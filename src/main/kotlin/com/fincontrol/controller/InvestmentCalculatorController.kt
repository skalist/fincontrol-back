package com.fincontrol.controller

import com.fincontrol.dto.InvestmentCalculatorDto
import com.fincontrol.service.InvestmentCalculatorService
import org.springframework.web.bind.annotation.*

/**
 * Controller for working with investment calculator
 */
@RestController
@RequestMapping("investment-calculator")
class InvestmentCalculatorController(
    private val investmentCalculatorService: InvestmentCalculatorService,
) {
    /**
     * Getting entity of investment calculator for current user
     * @return dto of investment calculator
     */
    @GetMapping
    fun findOne(): InvestmentCalculatorDto? {
        return investmentCalculatorService.findOne()
    }

    /**
     * Create or update investment calculator for current user
     * @param dto info about investment calculator
     */
    @PostMapping
    fun createOrUpdate(@RequestBody dto: InvestmentCalculatorDto) {
        investmentCalculatorService.createOrUpdate(dto)
    }

    /**
     * Method for getting info how much do we need invest for getting good live in retired age
     * @param investmentRequest info about inflation rate, investment return rate and desire salary
     * @return info about investing
     */
    @PostMapping("calculate")
    fun calculate(@RequestBody investmentRequest: InvestmentCalculatorDto) =
        investmentCalculatorService.calculate(investmentRequest)
}
