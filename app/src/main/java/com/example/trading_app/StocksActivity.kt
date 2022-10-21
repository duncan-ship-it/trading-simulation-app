package com.example.trading_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trading_app.databinding.ActivityMainBinding
import com.example.trading_app.databinding.ActivityStocksBinding


class StocksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityStocksBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}