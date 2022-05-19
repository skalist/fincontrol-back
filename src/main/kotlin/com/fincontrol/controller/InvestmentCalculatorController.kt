package com.fincontrol.controller

import com.fincontrol.dto.InvestmentCalculatorRequest
import com.fincontrol.service.InvestmentCalculatorService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for working with investment calculator
 */
@RestController
@RequestMapping("investment-calculator")
class InvestmentCalculatorController(
    private val investmentCalculatorService: InvestmentCalculatorService,
) {
    /**
     * Method for getting info how much do we need invest for getting good live in retired age
     * @param investmentRequest info about inflation rate, investment return rate and desire salary
     * @return info about investing
     */
    @PostMapping("calculate")
    fun calculate(@RequestBody investmentRequest: InvestmentCalculatorRequest) =
        investmentCalculatorService.calculate(investmentRequest)
}
