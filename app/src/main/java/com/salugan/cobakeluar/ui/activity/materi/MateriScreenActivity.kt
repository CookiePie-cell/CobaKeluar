package com.salugan.cobakeluar.ui.activity.materi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityMainBinding
import com.salugan.cobakeluar.databinding.ActivityMateriScreenBinding
import com.salugan.cobakeluar.ui.activity.history.ActivityHistory
import com.salugan.cobakeluar.ui.activity.soal.SoalActivity

class MateriScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMateriScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnDataDanKetidakpastian.setOnClickListener {
            startActivity(Intent(this@MateriScreenActivity, SoalActivity::class.java))
        }

        binding.btnHistory.setOnClickListener({
            startActivity(Intent(this@MateriScreenActivity, ActivityHistory::class.java))

        })

        binding.btnTataCara.setOnClickListener({
            Toast.makeText(this, "DIKERJAIN!!", Toast.LENGTH_LONG).show()
        })

    }
}