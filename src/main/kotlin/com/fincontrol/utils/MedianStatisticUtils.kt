package com.fincontrol.utils

import java.math.BigDecimal

class MedianStatisticUtils {
    companion object {
        fun getMedianSeries(values: List<BigDecimal>): List<BigDecimal> {
            val sortedValues = values.sorted()
            if (sortedValues.size == 1) {
                return List(5) { sortedValues[0] }
            }
            if (sortedValues.size == 2) {
                val middleValue = sortedValues[0].plus(sortedValues[1]).divide(BigDecimal(2))
                return listOf(sortedValues.first(), sortedValues.first(), middleValue, sortedValues.last(), sortedValues.last())
            }
            val middleValue = getMiddleValue(sortedValues)
            val (q1, q2) = getQuarterValues(sortedValues)
            return listOf(sortedValues.first(), q1, middleValue, q2, sortedValues.last())
        }

        private fun getMiddleValue(values: List<BigDecimal>): BigDecimal {
            if (values.size % 2 == 0) {
                val firstIndex = (values.size / 2) - 1
                val secondIndex = values.size / 2
                return values[firstIndex].plus(values[secondIndex]).divide(BigDecimal(2))
            }
            return values[values.size / 2]
        }

        private fun getQuarterValues(values: List<BigDecimal>): Pair<BigDecimal, BigDecimal> {
            val q1: BigDecimal
            val q2: BigDecimal
            if (values.size % 2 == 0) {
                q1 = getMiddleValue(values.subList(0, values.size / 2))
                q2 = getMiddleValue(values.subList(values.size / 2, values.size))
            } else {
                q1 = getMiddleValue(values.subList(0, values.size / 2 + 1))
                q2 = getMiddleValue(values.subList(values.size / 2, values.size))
            }

            return Pair(q1, q2)
        }
    }
}
