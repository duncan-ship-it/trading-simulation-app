package com.example.trading_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.trading_app.databinding.ActivityMainBinding
import com.google.api.Http
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioButton.setOnClickListener {
            startActivity(Intent(this, PortfolioActivity::class.java))
        }

        binding.tradesButton.setOnClickListener {
            startActivity(Intent(this, StocksActivity::class.java))
        }

        // retrieve stock symbols from file (used to look up current data in API)
        val reader = resources.openRawResource(R.raw.stocks).bufferedReader()
        val symbols = reader.use() { it.readText() }.split("\n")

        val model: StockViewModel by viewModels {
            StockViewModel.Factory(symbols.toTypedArray())
        }

        val stockObserver = Observer<List<Stock>> {
            println(it[1].tngoLast)
        }

        model.getStockData().observe(this, stockObserver)
    }
}