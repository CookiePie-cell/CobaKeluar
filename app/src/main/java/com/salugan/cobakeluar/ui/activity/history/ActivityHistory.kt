package com.salugan.cobakeluar.ui.activity.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.salugan.cobakeluar.databinding.ActivityHistoryBinding
import com.salugan.cobakeluar.databinding.ActivityLoginBinding

class ActivityHistory: AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val score = intent.getIntExtra(SCORE, 0)

        binding.nilaiData.text = score.toString()
    }

    companion object {
        const val SCORE = "extra_score"
    }

}