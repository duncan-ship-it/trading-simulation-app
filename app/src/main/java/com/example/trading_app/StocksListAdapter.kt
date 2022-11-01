package com.example.trading_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class StocksListAdapter(private val data: List<CurrentStockData>,
                        private val listener: (CurrentStockData) -> Unit)
        : RecyclerView.Adapter<StocksListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksListAdapter.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.stock_offer_row, parent, false) as View

            return ViewHolder(view)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: StocksListAdapter.ViewHolder, position: Int) {
            val item = data[position]
            holder.bind(item)
        }

        inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
            private val symbolView: TextView = v.findViewById(R.id.symbol)
            private val changeView: TextView = v.findViewById(R.id.change)
            private val costView: TextView = v.findViewById(R.id.cost)
            private val volumeView: TextView = v.findViewById(R.id.volume)

            fun bind(item: CurrentStockData) {
                val change = calculateChange(item.open, item.tngoLast)

                // use change figure if not null
                change?.let {
                    val changeTextTemplate = changeView.context.resources
                        .getString(R.string.change_value)
                    changeView.text = String.format(changeTextTemplate, it)

                    // set text color of change amount based on it's value
                    changeView.setTextColor(
                        Color.parseColor(
                            if (it < 0.toDouble()) "#cc0000" else "#33cc33" // red bad green good
                        )
                    )
                }

                // set other 3 fields
                symbolView.text = item.ticker
                costView.text = item.tngoLast.toString()

                // add commas to volume for readability
                volumeView.text = "%,d".format(item.volume)

                v.setOnClickListener { listener(item) }  // on click go to stock detail activity
            }

            // get percentage change between opening value and current value
            private fun calculateChange(open: Double?, current: Double?): Double? {
                open?.let { openNotNull ->
                    current?.let { currentNotNull ->
                        val change = (currentNotNull - openNotNull) / openNotNull
                        return (change * 100.0).roundToInt() / 100.0  // round to 2 decimal places
                    }
                }
                return null  // open or close were null
            }
        }
    }
