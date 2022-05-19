package com.fincontrol.model

/**
 * Asset event enumeration
 */
enum class AssetEvent(
    val value: String,
) {
    BUYING("Покупка"),
    SALE("Продажа"),
    PRICE_FIXING("Фиксирование стоимости"),
    DIVIDENDS("Дивиденды"),
    COUPONS("Купоны"),
}
