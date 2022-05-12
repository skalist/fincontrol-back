package com.fincontrol.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MedianStatisticUtilsTest {
    @Test
    fun `should return series for one element`() {
        val values = listOf(BigDecimal.ONE)
        val series = MedianStatisticUtils.getMedianSeries(values)
        assertThat(series).isEqualTo(
            listOf(
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE,
            )
        )
    }

    @Test
    fun `should return series for two elements`() {
        val values = listOf(BigDecimal(5), BigDecimal(20))
        val series = MedianStatisticUtils.getMedianSeries(values)
        assertThat(series).isEqualTo(
            listOf(
                BigDecimal(5),
                BigDecimal(5),
                BigDecimal(12.5),
                BigDecimal(20),
                BigDecimal(20),
            )
        )
    }

    @Test
    fun `should return series for three elements`() {
        val values = listOf(BigDecimal(5), BigDecimal(10), BigDecimal(15))
        val series = MedianStatisticUtils.getMedianSeries(values)
        assertThat(series).isEqualTo(
            listOf(
                BigDecimal(5),
                BigDecimal(7.5),
                BigDecimal(10),
                BigDecimal(12.5),
                BigDecimal(15),
            )
        )
    }

    @Test
    fun `should return series for four elements`() {
        val values = listOf(BigDecimal(5), BigDecimal(10), BigDecimal(15), BigDecimal(20))
        val series = MedianStatisticUtils.getMedianSeries(values)
        assertThat(series).isEqualTo(
            listOf(
                BigDecimal(5),
                BigDecimal(7.5),
                BigDecimal(12.5),
                BigDecimal(17.5),
                BigDecimal(20),
            )
        )
    }

    @Test
    fun `should return series for ten elements`() {
        val values = listOf(
            BigDecimal(5),
            BigDecimal(10),
            BigDecimal(15),
            BigDecimal(20),
            BigDecimal(25),
            BigDecimal(30),
            BigDecimal(35),
            BigDecimal(40),
            BigDecimal(45),
            BigDecimal(50),
        )
        val series = MedianStatisticUtils.getMedianSeries(values)
        assertThat(series).isEqualTo(
            listOf(
                BigDecimal(5),
                BigDecimal(15),
                BigDecimal(27.5),
                BigDecimal(40),
                BigDecimal(50),
            )
        )
    }

    @Test
    fun `should return series for not ordered elements`() {
        val values = listOf(
            BigDecimal(10),
            BigDecimal(20),
            BigDecimal(5),
            BigDecimal(15),
            BigDecimal(30),
            BigDecimal(45),
            BigDecimal(35),
            BigDecimal(25),
            BigDecimal(50),
            BigDecimal(40),
        )
        val series = MedianStatisticUtils.getMedianSeries(values)
        assertThat(series).isEqualTo(
            listOf(
                BigDecimal(5),
                BigDecimal(15),
                BigDecimal(27.5),
                BigDecimal(40),
                BigDecimal(50),
            )
        )
    }
}
