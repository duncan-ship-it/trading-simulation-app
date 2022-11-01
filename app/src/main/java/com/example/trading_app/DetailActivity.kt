package com.example.trading_app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.trading_app.databinding.ActivityDetailBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {
    private val lineChart: LineChart by lazy {
        findViewById(R.id.historicalData)
    }

    private val token: String by lazy {
        resources.getString(R.string.AUTH_TOKEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val symbol = intent.getStringExtra("symbol")
        val sellPrice = intent.getDoubleExtra("sellPrice", 0.0)
        val buyPrice = intent.getDoubleExtra("buyPrice", 0.0)

        binding.symbolName.text = symbol  // set name of stock

        binding.sellPrompt.text = String.format(resources.getString(
            R.string.sell_at_text),
            sellPrice.toString()
        )

        binding.buyPrompt.text = String.format(resources.getString(
            R.string.buy_at_text),
            buyPrice.toString()
        )

        val db = Firebase.firestore  // db reference used to C/R/U/D order documents in listeners

        binding.buyButton.setOnClickListener {
            val parsedQty = binding.buyQuantity.text.toString().toIntOrNull()

            if (parsedQty != null) {
                val newOrder = hashMapOf(
                    "symbol" to symbol,
                    "qty" to parsedQty,
                    "price" to buyPrice
                )
                // create a new order with specified quantity
                db.collection("orders")
                    .add(newOrder)
                    .addOnSuccessListener {
                        toast("Created order $parsedQty units of $symbol") // notify user of success
                    }
            }
            else {
                toast("Invalid buy order quantity")
            }
        }

        binding.sellButton.setOnClickListener {
            val parsedQty = binding.sellQty.text.toString().toLongOrNull()
            if (parsedQty != null) {
                var sellUnitsLeft = parsedQty.toLong()

                // loop through orders of stock and sell units until none are left or out of orders
                db.collection("orders")
                    .whereEqualTo("symbol", symbol)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            for (order in result) {
                                val orderQty = order.get("qty") as Long

                                if (orderQty > sellUnitsLeft) {
                                    // take off sold units on order
                                    order.reference.update("qty", orderQty - sellUnitsLeft)
                                    sellUnitsLeft = 0
                                    break
                                }

                                order.reference.delete()  // all units in order sold

                                sellUnitsLeft -= orderQty
                            }
                            toast("Sold ${parsedQty - sellUnitsLeft} of $parsedQty units")
                        }
                        else {
                            toast("Invalid sell order; you own none of these units!")
                        }
                    }
            }
            else {
                toast("Sell order quantity should be a number!")
            }
        }

        getHistoricalData(symbol!!) { historicalData ->
            val values = arrayListOf<Entry>()
            val dateTimeLabels = mutableListOf<String>()

            val f = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.JAPAN)

            // generate points containing date and price
            for (epoch in historicalData) {
                val date = f.parse(epoch.date)
                val point = Entry(date!!.time.toFloat(), epoch.open)

                values.add(point)
                dateTimeLabels.add(epoch.date)
            }

            lineChart.xAxis.valueFormatter = (object : IndexAxisValueFormatter(dateTimeLabels.toTypedArray()) {
                override fun getFormattedValue(value: Float, axis: AxisBase): String {
                    return Date(value.toLong()).toString()
                }
            })

            val lineData = LineDataSet(values, "${symbol} opening prices")

            lineChart.data = LineData(lineData)  // set data on line chart
        }
    }

    // retrieve historical data of the symbol using api
    private fun getHistoricalData(symbol: String,
                                  onSuccess: (data: List<HistoricalStockData>) -> Unit) {
        val client = OkHttpClient()  // client http object used to make historical data requests

        val time = Calendar.getInstance().time.time - (60 * 60 * 24 * 7 * 1000)  // a week ago
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = formatter.format(time)

        val url = "https://api.tiingo.com/iex/$symbol/prices?startDate=$currentDate&resampleFreq=" +
                "4hour&token=$token"

        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)

        call.enqueue(object: Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()

                responseBody?.let { body ->
                    val historicalData = Json.decodeFromString<List<HistoricalStockData>>(body)
                    onSuccess(historicalData)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                toast("An error has occurred. Please restart the app.")
            }
        })
    }

    // convenient extension method for making toasts using the activity context
    // https://stackoverflow.com/questions/36826004/how-do-you-display-a-toast-using-kotlin-on-android#answer-36826336
    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}