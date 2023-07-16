package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbs.mbsapp.databinding.ActivityEndBinding

class EndActivity : AppCompatActivity() {

    lateinit var binding : ActivityEndBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            this@EndActivity.finish()
        }

        binding.logout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.EndActivity.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }
    }
}