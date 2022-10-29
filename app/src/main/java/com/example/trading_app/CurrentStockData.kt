package com.example.trading_app

import kotlinx.serialization.Serializable

@Serializable
data class CurrentStockData(
    val ticker: String,
    val timestamp: String,
    val quoteTimestamp: String?,
    val lastSaleTimestamp: String?,
    val last: Double?,
    val lastSize: Int?,
    val tngoLast: Double,
    val prevClose: Double,
    val open: Double,
    val high: Double,
    val low: Double,
    val mid: Double?,
    val volume: Int,
    val bidSize: Int?,
    val bidPrice: Double?,
    val askSize: Double?,
    val askPrice: Double?
)