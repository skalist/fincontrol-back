package com.fincontrol.model

enum class AssetEvent(
    val value: String,
) {
    BUYING("Покупка"),
    SALE("Продажа"),
    PRICE_FIXING("Фиксирование стоимости"),
    DIVIDENDS("Дивиденды"),
    COUPONS("Купоны"),
}
