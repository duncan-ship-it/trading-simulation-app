package com.example.trading_app
import kotlinx.serialization.Serializable


@Serializable
data class HistoricalStockData(
    val date: String,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float
)
