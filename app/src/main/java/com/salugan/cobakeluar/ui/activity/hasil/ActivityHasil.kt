package com.salugan.cobakeluar.ui.activity.hasil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityHasilToBinding
import com.salugan.cobakeluar.utils.DateTimeUtils

class ActivityHasil: AppCompatActivity() {
    private lateinit var binding: ActivityHasilToBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilToBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val score = intent.getIntExtra(SCORE, 0)
        val answers = intent.getIntegerArrayListExtra(ANSWERS)
        val completionTime = intent.getStringExtra(COMPLETION_TIME)
        binding.nilai.text = (score * 10).toString()

        binding.tanggal.text = DateTimeUtils.getCurrentDate()

        binding.waktu.text = completionTime

        if (answers != null) {
            binding.benar.text = String.format(getString(R.string.benar), answers.count { it == 1})
            binding.salah.text = String.format(getString(R.string.salah), answers.count { it == 2})
            binding.kosong.text = String.format(getString(R.string.kosong), answers.count { it == 0})
        }
    }

    companion object {
        const val SCORE = "extra_score"
        const val ANSWERS = "extra_answers"
        const val COMPLETION_TIME = "extra_completion_time"
    }
}