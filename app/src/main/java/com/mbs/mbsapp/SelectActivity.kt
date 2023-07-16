package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbs.mbsapp.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {

    lateinit var binding : ActivitySelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.next.setOnClickListener {
            val intent = Intent(this, ClusterStartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);

        }


    }
}