package com.example.trading_app

import androidx.lifecycle.*

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class StockViewModel(private val token: String): ViewModel() {
    private val client = OkHttpClient()

    private val stocks =  MutableLiveData<List<CurrentStockData>>().also {
        it.value = listOf()  // initialise value as empty list (for adapter)
    }

    // retrieve current stock data from multiple symbols asynchronously from api
    fun loadStockData(symbols: Array<String>) {
        val tickers = symbols.reduce{acc, next -> acc + ",${next}"}
        val url = "https://api.tiingo.com/iex/?tickers=${tickers}&token=${token}"

        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)

        call.enqueue(object: Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()

                responseBody?.let { body ->
                    stocks.postValue(Json.decodeFromString<List<CurrentStockData>>(body)
                        .sortedBy { it.ticker })
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("failure :(")
            }
        })
    }

    // allow access to the live stocks data so it can be observed
    fun getStockData(): LiveData<List<CurrentStockData>> {
        return stocks
    }

    class Factory(private val token: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StockViewModel(token) as T
        }
    }
}
