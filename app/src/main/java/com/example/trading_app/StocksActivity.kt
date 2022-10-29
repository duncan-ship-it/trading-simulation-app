package com.example.trading_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trading_app.databinding.ActivityStocksBinding


class StocksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve all stock symbols from csv file (used to look up current data in API)
        val reader = resources.openRawResource(R.raw.stocks).bufferedReader()
        val symbols = reader.use { it.readText() }.split("\n")

        val token = resources.getString(R.string.AUTH_TOKEN)  // hidden in values/secrets.xml

        val model: StockViewModel by viewModels {
            StockViewModel.Factory(token)
        }

        model.loadStockData(symbols.toTypedArray())

        binding.stocksView.adapter = StocksListAdapter(model.getStockData().value!!) {
            // go to stock detail activity, using symbol as reference
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("symbol", it.ticker)
                startActivity(this)
            }
        }

        binding.stocksView.layoutManager = LinearLayoutManager(this)

        // update recyclerview list once stock data has been updated
        val stockUpdateObserver = Observer<List<CurrentStockData>> {
            binding.stocksView.adapter = StocksListAdapter(model.getStockData().value!!) {

                // go to stock detail activity, using symbol as reference
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("symbol", it.ticker)
                    putExtra("buyPrice", it.tngoLast)
                    putExtra("sellPrice", it.prevClose)
                }

                startActivity(intent)
            }
        }

        model.getStockData().observe(this, stockUpdateObserver)
    }
}