package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Adapters.StockAdapter
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityStockGiftCountBinding

class Stock_Gift_CountActivity : AppCompatActivity() {
    lateinit var binding: ActivityStockGiftCountBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockGiftCountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)
        var campaignId = tinydb.getInt("campaignId")

        var productlist = mbsDatabase.getMBSData().getProducts(campaignId)

        binding.recyclerview.adapter = StockAdapter(context = this, productlist)

        binding.back.setOnClickListener {
            this@Stock_Gift_CountActivity.finish()
        }

        binding.logout.setOnClickListener {
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
            Toast.makeText(this@Stock_Gift_CountActivity, "Product Stock Submitted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }
    }
}