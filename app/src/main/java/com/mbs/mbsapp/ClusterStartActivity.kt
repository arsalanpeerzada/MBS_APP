package com.mbs.mbsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mbs.mbsapp.databinding.ActivityClusterStartBinding

class ClusterStartActivity : AppCompatActivity() {

    lateinit var binding : ActivityClusterStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClusterStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startActivity.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            finish()
        }

        binding.back.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.right2, R.anim.right);
        }

        binding.logout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }
    }
}