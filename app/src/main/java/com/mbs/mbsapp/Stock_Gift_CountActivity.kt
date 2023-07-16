package com.mbs.mbsapp

import android.content.Intent
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

        binding.logout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            this@Stock_Gift_CountActivity.finish()
        }

        binding.submit.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }
    }
}