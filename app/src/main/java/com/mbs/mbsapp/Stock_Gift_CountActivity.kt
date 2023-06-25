package com.mbs.mbsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbs.mbsapp.Adapters.StockAdapter
import com.mbs.mbsapp.databinding.ActivityQuestionnaireBinding
import com.mbs.mbsapp.databinding.ActivityStockGiftCountBinding

class Stock_Gift_CountActivity : AppCompatActivity() {
    lateinit var binding : ActivityStockGiftCountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockGiftCountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.adapter = StockAdapter(context = this)

        binding.back.setOnClickListener {
            this@Stock_Gift_CountActivity.finish()
        }
    }
}