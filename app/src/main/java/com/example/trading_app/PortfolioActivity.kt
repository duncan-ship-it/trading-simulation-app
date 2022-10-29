package com.example.trading_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trading_app.databinding.ActivityPortfolioBinding
import com.google.firebase.firestore.FirebaseFirestore


class PortfolioActivity : AppCompatActivity() {
    private var holdings = MutableLiveData<List<StockOrder>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPortfolioBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.holdingsList.adapter = PortfolioListAdapter(listOf())
        binding.holdingsList.layoutManager = LinearLayoutManager(this)

        val holdingObserver = Observer<List<StockOrder>> {
            holdings.value?.let {
                binding.holdingsList.adapter = PortfolioListAdapter(it)
            }
        }

        holdings.observe(this, holdingObserver)

        val db = FirebaseFirestore.getInstance()

        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                holdings.value = result.documents.map{
                    StockOrder(
                        it.get("symbol") as String,
                        it.get("qty") as Long,
                        it.get("price") as Double
                    )
                }
                println("size: ${holdings.value!!.size}")
            }
    }
}