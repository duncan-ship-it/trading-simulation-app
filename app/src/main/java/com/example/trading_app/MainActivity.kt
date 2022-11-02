package com.example.trading_app


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannedString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.trading_app.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.max
import java.math.RoundingMode
import kotlin.math.min


class MainActivity : AppCompatActivity() {
    private var orders = MutableLiveData<List<StockOrder>>()

    private val db = Firebase.firestore

    private val model: StockViewModel by viewModels {
        StockViewModel.Factory(resources.getString(R.string.AUTH_TOKEN))
    }

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

        binding.resetButton.setOnClickListener {
            // remove all orders from collection
            db.collection("orders")
                .get()
                .addOnSuccessListener { orders ->
                    for (document in orders) {
                        document.reference.delete()
                    }
                }

            orders.value = listOf()  // reset orders
        }

        // check for data updates from stock data view model
        val stockDataObserver = Observer<List<CurrentStockData>> { stockData ->
            var portfolioValue = 0f
            var totalInvested = 0f

            orders.value?.let { orders ->
                for (order in orders) {
                    val currentPrice = stockData.filter { it.ticker == order.symbol}[0].tngoLast

                    totalInvested += (order.qty * order.price).toFloat()
                    portfolioValue += (order.qty * currentPrice).toFloat()
                }
            }

            updateFinances(portfolioValue, totalInvested)
        }

        val stockOrderObserver = Observer<List<StockOrder>> { orders ->
            if (orders.isNotEmpty()) {
                val orderSymbols = orders.map{it.symbol}.toTypedArray()
                model.loadStockData(orderSymbols)
            }
            else {
                updateFinances(0f, 0f)
            }
        }

        model.getStockData().observe(this, stockDataObserver)
        orders.observe(this, stockOrderObserver)
    }

    // update stock orders
    override fun onResume() {
        getDatabaseOrders()  // sync orders with firebase (and trigger observers)
        super.onResume()
    }

    // retrieve user-made stock orders from database
    private fun getDatabaseOrders() {
        val db = Firebase.firestore
        val dbOrders = mutableListOf<StockOrder>()

        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dbOrders.add(
                        StockOrder(
                            document.get("symbol") as String,
                            document.get("qty") as Long,
                            document.get("price") as Double
                        )
                    )
                }
                // observer is triggered once per call
                this.orders.value = dbOrders
            }
    }

    // update financial information views (also change background color)
    private fun updateFinances(portfolioValue: Float, totalInvested: Float) {
        // percentage increase of current value
        val netGain = (portfolioValue - totalInvested) * 100 /
                if (portfolioValue == 0f) 1f else portfolioValue

        findViewById<TextView>(R.id.investingTotal).text = String.format(
            resources.getString(R.string.investing_amount),
            portfolioValue.toString()
        )

        val roundedNetGain = netGain
            .toBigDecimal()
            .setScale(2, RoundingMode.HALF_EVEN)
            .toFloat()

        findViewById<TextView>(R.id.netGainFigure).text = String.format(
            resources.getString(R.string.change_value),
            roundedNetGain
        )

        orders.value?.let { orders ->
            model.getStockData().value?.let { allStockData ->
                var marketReport = SpannableString("")

                for (symbol in orders.map{it.symbol}.toSet()) {
                    val stock = allStockData.filter { it.ticker == symbol}[0]

                    marketReport = SpannableString(
                        TextUtils.concat(marketReport, "  ", getSymbolSummary(stock))
                    )
                }
                findViewById<TextView>(R.id.scrollingSummary).text = marketReport
            }
        }

        // update color of background based on net gain
        findViewById<ConstraintLayout>(R.id.background).setBackgroundColor(
            Color.parseColor(
                hexColorOf(netGain)
            )
        )
    }

    private fun getSymbolSummary(symbolData: CurrentStockData): SpannedString {
        val isProfitable = symbolData.tngoLast > symbolData.open

        val colorRef = if (isProfitable) R.color.epic_green else R.color.not_epic_red

        return buildSpannedString {
            bold {
                inSpans(ForegroundColorSpan(ContextCompat.getColor(applicationContext, colorRef))) {
                    append("${symbolData.ticker} ${if (isProfitable) "▲" else "▼"}")
                }
            }
        }
    }

    // return a hex color string representing the performance of the user (green to red)
    private fun hexColorOf(net: Float): String {
        var red = 200
        var green = 200

        if (net > 0) {
            red = min((255 * net / 100).toInt(),200)
        }
        else if (net < 0) {
            green = min((255 * -net / 100).toInt(), 200)
        }

        return String.format(
            "#%s%s4c",
            Integer.toHexString(red).padStart(2, '0'),
            Integer.toHexString(green).padStart(2, '0')
        )

    }
}
