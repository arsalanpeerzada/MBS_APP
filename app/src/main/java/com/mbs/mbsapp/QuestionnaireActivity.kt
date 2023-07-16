package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mbs.mbsapp.Adapters.QuestionAdapter
import com.mbs.mbsapp.databinding.ActivityEndBinding
import com.mbs.mbsapp.databinding.ActivityQuestionnaireBinding

class QuestionnaireActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionnaireBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.adapter = QuestionAdapter(this)

        binding.back.setOnClickListener {
            this@QuestionnaireActivity.finish()
        }

        binding.logout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.left, R.anim.left2);
            this.finish()
        }

        binding.close.setOnClickListener {
            this@QuestionnaireActivity.finish()
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