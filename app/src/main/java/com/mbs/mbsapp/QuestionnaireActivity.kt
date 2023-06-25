package com.mbs.mbsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}