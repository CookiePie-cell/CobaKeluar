package com.salugan.cobakeluar.ui.activity.hasil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.salugan.cobakeluar.databinding.ActivityHasilToBinding
import com.salugan.cobakeluar.ui.activity.history.ActivityHistory

class ActivityHasil: AppCompatActivity() {
    private lateinit var binding: ActivityHasilToBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilToBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }


}