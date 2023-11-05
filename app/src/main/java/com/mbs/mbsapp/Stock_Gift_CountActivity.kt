package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Adapters.StockAdapter
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.Database.Entities.ProductStock
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityStockGiftCountBinding

class Stock_Gift_CountActivity : AppCompatActivity() {
    lateinit var binding: ActivityStockGiftCountBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    var campaignId: Int = 0
    var activityId: Int = 0
    var activityDetailId: Int = 0
    lateinit var productlist: List<ProductEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockGiftCountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)

        campaignId = tinydb.getInt("campaignId")
        activityId = tinydb.getInt("activitymasterid")
        activityDetailId = tinydb.getInt("activitydetailid")


        var getproducts = mbsDatabase.getMBSData().getProductStocks(campaignId, activityDetailId)



        productlist = mbsDatabase.getMBSData().getProducts(campaignId)

        binding.recyclerview.adapter = StockAdapter(context = this, productlist, getproducts)

        binding.back.setOnClickListener {
            binding.submit.performClick()

        }

        binding.logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            binding.submit.performClick()

        }

        binding.submit.setOnClickListener {
            Toast.makeText(
                this@Stock_Gift_CountActivity,
                "Product Stock Submitted",
                Toast.LENGTH_SHORT
            ).show()
            insertIntoDatabae(productlist)
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }
    }

    private fun insertIntoDatabae(productlist: List<ProductEntity>) {

        var productCount = 0
        for (item in productlist) {

            var productStock = ProductStock(
                productCount,
                productCount,
                campaignId,
                activityId,
                activityDetailId,
                item.id,
                item.productAnswer,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
            )
            productCount++
            mbsDatabase.getMBSData().insertProductStocks(productStock)


        }

    }

    override fun onBackPressed() {

    }
}