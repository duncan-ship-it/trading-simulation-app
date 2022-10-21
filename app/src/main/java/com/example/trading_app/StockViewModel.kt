package com.example.trading_app

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.google.common.base.Ticker
import com.google.common.eventbus.Subscribe

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException


class StockViewModel(private val symbols: Array<String>): ViewModel() {
    private val client = OkHttpClient()

    private val stocks =  MutableLiveData<List<Stock>>().also {
        loadStockData()
    }

    // retrieve symbol data asynchronously from api
    private fun loadStockData() {
        val tickers = symbols.reduce{acc, next -> acc + ",${next}"}
        val url = "https://api.tiingo.com/iex/?tickers=${tickers}&token=${R.string.AUTH_TOKEN}"

        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)

        call.enqueue(object: Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()

                responseBody?.let {
                    val res = Json.decodeFromString<List<Stock>>(it)

                    stocks.postValue(res)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("failure :(")
            }
        })
    }

    fun getStockData(): LiveData<List<Stock>> {
        return stocks  // allow access to the livedata so it can be observed
    }

    class Factory(private val symbols: Array<String>) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StockViewModel(symbols) as T
        }
    }
}
