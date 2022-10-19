package com.example.trading_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import yahoofinance.Stock
import yahoofinance.YahooFinance


class StockViewModel(private val symbols: Array<String>): ViewModel() {
    private val stocks: MutableLiveData<MutableMap<String, Stock>> by lazy {
        MutableLiveData<MutableMap<String, Stock>>().also {
            loadStockData()  // fetch stock data on initialisation
        }
    }

    fun getStockData(): LiveData<MutableMap<String, Stock>> {
        return stocks  // access the livedata so it can be observed
    }

    private fun loadStockData() {
        // start a new thread so synchronous get request does not block the main thread
        val stockThread = Thread {

            val actual = mutableListOf("T")

            for (i in 1..1) {
                actual.add(symbols[0])
                actual.add(symbols[1])
            }

            println(actual.size)

            val data = YahooFinance.get(actual.toTypedArray())

            println(data.size)

            // set livedata to map containing symbol and stock object pairs
            stocks.postValue(data)
        }

        stockThread.start()
    }

    class Factory(private val symbols: Array<String>) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StockViewModel(symbols) as T
        }
    }
}
