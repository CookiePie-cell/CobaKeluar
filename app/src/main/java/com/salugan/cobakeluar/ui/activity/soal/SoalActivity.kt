package com.salugan.cobakeluar.ui.activity.soal

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
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
import com.salugan.cobakeluar.model.QuestionModel
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

        val kategori = intent.getIntExtra(KATEGORI, 0)

        if (kategori == 0) {
            setDataDanKetidakPastianTryOut()
        } else {
            setGeometriDanPengukuranTryOut()
        }
    }

    private fun setDataDanKetidakPastianTryOut() {
        soalViewModel.getDataDanKetidakPastianQuestion().observe(this) {
            when (it) {
                is Result.Loading -> ""
                is Result.Success -> {
                    Log.d("wkwkwk", it.data.toString())
                    val data: ArrayList<QuestionModel> = ArrayList(it.data)
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, data)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.isUserInputEnabled = false
                    viewPager.adapter = tabPagerSoalAdapter
                    val tabs: TabLayout = binding.tabs
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = (position + 1).toString()
                    }.attach()

                    tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            // Reset the scroll position to the top when a new tab is selected.
                            binding.nestedScrollView.fullScroll(View.FOCUS_UP)
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab) {}

                        override fun onTabReselected(tab: TabLayout.Tab) {}
                    })

                    binding.btnNext.setOnClickListener {
                        val currentItem = viewPager.currentItem
                        Log.d("dfgdfg", currentItem.toString())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    }

                    binding.btnPrev.setOnClickListener {
                        val currentItem = viewPager.currentItem
                        viewPager.setCurrentItem(currentItem - 1, true)
                    }
                    for (i in 0..9) {
                        val textView =
                            LayoutInflater.from(this).inflate(R.layout.tab_title, null) as TextView

                        binding.tabs.getTabAt(i)?.customView = textView
                    }
                }

                is Result.Error -> ""
            }
        }
    }


    private fun setGeometriDanPengukuranTryOut() {
        soalViewModel.getGeometriDanPengukuranQuestion().observe(this) {
            when (it) {
                is Result.Loading -> ""
                is Result.Success -> {
                    Log.d("wkwkwk", it.data.toString())
                    val data: ArrayList<QuestionModel> = ArrayList(it.data)
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, data)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.isUserInputEnabled = false
                    viewPager.adapter = tabPagerSoalAdapter
                    val tabs: TabLayout = binding.tabs
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = (position + 1).toString()
                    }.attach()

                    tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            // Reset the scroll position to the top when a new tab is selected.
                            binding.nestedScrollView.fullScroll(View.FOCUS_UP)
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab) {}

                        override fun onTabReselected(tab: TabLayout.Tab) {}
                    })

                    binding.btnNext.setOnClickListener {
                        val currentItem = viewPager.currentItem
                        Log.d("dfgdfg", currentItem.toString())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    }

                    binding.btnPrev.setOnClickListener {
                        val currentItem = viewPager.currentItem
                        viewPager.setCurrentItem(currentItem - 1, true)
                    }
                    for (i in 0..9) {
                        val textView =
                            LayoutInflater.from(this).inflate(R.layout.tab_title, null) as TextView

                        binding.tabs.getTabAt(i)?.customView = textView
                    }
                }

                is Result.Error -> ""
            }
        }
    }

    companion object {
        const val KATEGORI = "extra_kategori"
    }
}