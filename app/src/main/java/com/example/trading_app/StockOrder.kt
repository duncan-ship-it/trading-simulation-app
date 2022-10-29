package com.example.trading_app

data class StockOrder (
    val symbol: String,
    val qty: Long,
    val price: Double
)