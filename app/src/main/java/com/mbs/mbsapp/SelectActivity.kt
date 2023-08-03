package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.inksy.Database.MBSDatabase
import com.mbs.mbsapp.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {

    lateinit var binding : ActivitySelectBinding
    lateinit var mbsDatabase: MBSDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mbsDatabase = MBSDatabase.getInstance(this)!!

        binding.next.setOnClickListener {
            val intent = Intent(this, ClusterStartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);

        }

        var data = mbsDatabase.getMBSData().getAllBrands()
        Toast.makeText(this, "data fetched", Toast.LENGTH_SHORT).show()


    }
}