package com.salugan.cobakeluar.ui.activity.soal

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.TabPagerSoalAdapter
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivitySoalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoalBinding

//    private val tabTitles = arrayListOf("a", "b", "C")

    private val soalViewModel: SoalViewModel by viewModels()

    val tes = "wowkowkwowk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soalViewModel.getDataDanKetidakPastianQuestion().observe(this) {
            when(it) {
                is Result.Loading -> ""
                is Result.Success -> {
                    Log.d("wkwkwk", it.data.toString())
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, it.data.size)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.adapter = tabPagerSoalAdapter
                    val tabs: TabLayout = binding.tabs
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = (position + 1).toString()
                    }.attach()
                    for (i in 0..9) {
                        val textView = LayoutInflater.from(this).inflate(R.layout.tab_title, null) as TextView

                        binding.tabs.getTabAt(i)?.customView = textView
                    }
                }
                is Result.Error -> ""
            }
        }



    }


}