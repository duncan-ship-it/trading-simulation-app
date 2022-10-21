package com.example.trading_app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val ticker: String,
    val timestamp: String,
    val quoteTimestamp: String,
    val lastSaleTimestamp: String,
    val last: Float,
    val lastSize: Int?,
    val tngoLast: Float,
    val prevClose: Float,
    val open: Float,
    val high: Float,
    val low: Float,
    val mid: Float?,
    val volume: Int,
    val bidSize: Int?,
    val bidPrice: Float?,
    val askSize: Float?,
    val askPrice: Float?
)