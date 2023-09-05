package com.salugan.cobakeluar.ui.activity.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salugan.cobakeluar.databinding.ActivityHomeBinding
import com.salugan.cobakeluar.ui.activity.materi.MateriScreenActivity
import com.salugan.cobakeluar.ui.activity.profile.ActivityProfile

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconProfile.setOnClickListener(){

            var nama = intent.getStringExtra("nama")?:""
            var email = intent.getStringExtra("email")?: ""
            var phoneNumber = intent.getStringExtra("phoneNumber")?: ""
            var userPhoto = intent.getStringExtra("userPhoto")

            val intent = Intent(this, ActivityProfile::class.java)
            intent.putExtra("nama", nama)
            intent.putExtra("email", email)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("userPhoto", userPhoto)
            startActivity(intent)

        }

        binding.button.setOnClickListener {
            startActivity(Intent(this@HomeActivity, MateriScreenActivity::class.java))
        }
    }
}