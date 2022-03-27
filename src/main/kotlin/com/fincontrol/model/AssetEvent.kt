package com.fincontrol.model

enum class AssetEvent(
    val value: String,
) {
    BUYING("Полкупка"),
    SALE("Продажа"),
    PRICE_FIXING("Фиксирование стоимости"),
    DIVIDENDS("Дивиденды"),
    COUPONS("Купоны"),
}
