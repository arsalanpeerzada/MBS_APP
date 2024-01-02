package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.Utils.TinyDB
import com.mbs.mbsapp.databinding.ActivityCampaignBinding

class CampaignActivity : AppCompatActivity() {

    lateinit var binding: ActivityCampaignBinding
    lateinit var mbsDatabase: MBSDatabase
    lateinit var tinydb: TinyDB
    var brandId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!
        tinydb = TinyDB(this)
        brandId = tinydb.getInt("brandId")
        var data = mbsDatabase.getMBSData().getCampaignbyBrand(brandId)
        binding.textview.text = Html.fromHtml(data[0].campaignDescription)



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
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

    }
}