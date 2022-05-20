package com.fincontrol.controller

import com.fincontrol.ApplicationIT
import com.fincontrol.dto.InvestmentCalculatorRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

class InvestmentCalculatorControllerTest : ApplicationIT() {
    @Test
    fun `should calculate investment amount`() {
        val request =
            InvestmentCalculatorRequest(
                startAge = 1,
                retiredAge = 4,
                investmentReturnPercent = BigDecimal(20),
                inflationPercent = BigDecimal(10),
                expectedSalaryNowPerMonth = BigDecimal(100_000),
            )

        val response = investmentCalculatorController.calculate(request)
        assertThat(response.investmentPerMonth.setScale(0, RoundingMode.HALF_EVEN))
            .isEqualTo(BigDecimal(304_716))
        assertThat(response.retiredSalaryPerMonth.setScale(0, RoundingMode.HALF_EVEN))
            .isEqualTo(BigDecimal(133_100))
        assertThat(response.accumulatedInvestments.setScale(0, RoundingMode.HALF_EVEN))
            .isEqualTo(BigDecimal(15_972_000))
        assertThat(response.totalYears).isEqualTo(3)
    }
}
