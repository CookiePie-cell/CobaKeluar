package com.salugan.cobakeluar.ui.activity.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityMainBinding
import com.salugan.cobakeluar.ui.activity.materi.MateriScreenActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(this@MainActivity, MateriScreenActivity::class.java))
        }
    }
}