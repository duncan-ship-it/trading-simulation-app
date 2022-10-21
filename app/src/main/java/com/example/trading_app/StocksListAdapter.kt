package com.example.trading_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StocksListAdapter(private val data: List<List<String>>,
                        private val listener: (List<String>) -> Unit)
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

            fun bind(item: List<String>) {

                symbolView.text = item[0]
                changeView.text = item[1]
                costView.text = item[2]
                volumeView.text = item[3]

                v.setOnClickListener { listener(item) }
            }
        }
    }
