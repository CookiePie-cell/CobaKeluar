package com.salugan.cobakeluar.ui.activity.home


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.BannerAdapter
import com.salugan.cobakeluar.databinding.ActivityHomeBinding
import com.salugan.cobakeluar.ui.activity.authentication.ActivityLogin
import com.salugan.cobakeluar.ui.activity.materi.MateriScreenActivity
import com.salugan.cobakeluar.ui.activity.profile.ActivityProfile
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val autoSlideHandler = Handler()
    private val delayMillis: Long = 2000
    private var currentPage = 0
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = findViewById(R.id.viewPager)
        val dotsIndicator: WormDotsIndicator = findViewById(R.id.dotsIndicator)
        val imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            )
        val adapter = BannerAdapter(imageList)
        viewPager.adapter = adapter
        // Hubungkan ViewPager2 dengan ViewPager2Indicator
        dotsIndicator.setViewPager2(viewPager)
        // Memulai pemutaran otomatis
        startAutoSlider()
        binding.iconProfile.setOnClickListener() {
            val intent = Intent(this, ActivityProfile::class.java)
            startActivity(intent)
        }
        binding.iconFAQ.setOnClickListener({
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eurekaedutech.com/contact"))
            startActivity(intent)
        })
        binding.btnAbout.setOnClickListener({
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eurekaedutech.com/about"))
            startActivity(intent)
        })
        binding.button.setOnClickListener ({
            startActivity(Intent(this@HomeActivity, MateriScreenActivity::class.java))
        })
        binding.btnLiterasi.setOnClickListener({
            Toast.makeText(this, "Try out tidak tersedia", Toast.LENGTH_SHORT).show()
        })
    }
    private fun startAutoSlider() {
        val runnable = object : Runnable {
            override fun run() {
                if (currentPage == viewPager.adapter?.itemCount) {
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage, true)
                currentPage++
                autoSlideHandler.postDelayed(this, delayMillis)
            }
        }
        autoSlideHandler.postDelayed(runnable, delayMillis)
    }
    override fun onBackPressed() {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        val intent = Intent(this, ActivityLogin::class.java) // Replace with the appropriate activity
        startActivity(intent)
        finish()
    }
}
