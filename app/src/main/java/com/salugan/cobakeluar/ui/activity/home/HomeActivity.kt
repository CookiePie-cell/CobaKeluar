package com.salugan.cobakeluar.ui.activity.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityHomeBinding
import com.salugan.cobakeluar.ui.activity.materi.MateriScreenActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(this@HomeActivity, MateriScreenActivity::class.java))
        }
    }
}