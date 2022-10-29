package com.example.trading_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.math.RoundingMode
import java.util.*

class PortfolioListAdapter(private val data: List<StockOrder>)
    : RecyclerView.Adapter<PortfolioListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.portfolio_row, parent, false) as View

        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PortfolioListAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val symbolView: TextView = v.findViewById(R.id.symbol)
        private val qtyView: TextView = v.findViewById(R.id.qty)
        private val costView: TextView = v.findViewById(R.id.cost)

        fun bind(item: StockOrder) {
            symbolView.text = item.symbol
            qtyView.text = item.qty.toString()
            costView.text = String.format(
                Locale.getDefault(),
                "%.4f",
                (item.qty * item.price).toFloat()
            )
        }
    }
}