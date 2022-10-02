package com.example.trading_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trading_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.portfolioButton.setOnClickListener {
            startActivity(Intent(this, PortfolioActivity::class.java))
        }

        binding.tradesButton.setOnClickListener {
            startActivity(Intent(this, StocksActivity::class.java))
        }

        setContentView(binding.root)
    }
}