package com.fincontrol.model

/**
 * Asset type enumeration
 */
enum class AssetType(
    val value: String,
) {
    STOCK("Акция"),
    BOND("Облигация"),
    ETF("ETF"),
}
