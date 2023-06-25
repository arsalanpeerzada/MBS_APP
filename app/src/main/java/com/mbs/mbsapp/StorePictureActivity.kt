package com.mbs.mbsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbs.mbsapp.databinding.ActivityStockGiftCountBinding
import com.mbs.mbsapp.databinding.ActivityStorePictureBinding

class StorePictureActivity : AppCompatActivity() {
    lateinit var binding : ActivityStorePictureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            this@StorePictureActivity.finish()
        }
    }
}