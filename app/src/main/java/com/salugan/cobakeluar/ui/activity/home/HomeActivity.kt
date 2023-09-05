package com.salugan.cobakeluar.ui.activity.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.BannerAdapter
import com.salugan.cobakeluar.databinding.ActivityHomeBinding
import com.salugan.cobakeluar.ui.activity.materi.MateriScreenActivity
import com.salugan.cobakeluar.ui.activity.profile.ActivityProfile
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var nama = intent.getStringExtra("nama")?:""
        var email = intent.getStringExtra("email")?: ""
        var phoneNumber = intent.getStringExtra("phoneNumber")?: ""
        var userPhoto = intent.getStringExtra("userPhoto")

        binding.nama.text = nama

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val dotsIndicator: WormDotsIndicator = findViewById(R.id.dotsIndicator)

        val imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )

        val adapter = BannerAdapter(imageList)
        viewPager.adapter = adapter

        // Hubungkan ViewPager2 dengan ViewPager2Indicator
        dotsIndicator.setViewPager2(viewPager)

        binding.iconProfile.setOnClickListener(){

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