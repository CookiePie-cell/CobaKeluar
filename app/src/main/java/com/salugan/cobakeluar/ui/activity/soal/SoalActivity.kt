package com.salugan.cobakeluar.ui.activity.soal

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kennyc.view.MultiStateView
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.TabPagerSoalAdapter
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivitySoalBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.ui.activity.hasil.ActivityHasil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SoalActivity : AppCompatActivity(), MultiStateView.StateListener {

    private lateinit var binding: ActivitySoalBinding

    private lateinit var multiStateView: MultiStateView

//    private val tabTitles = arrayListOf("a", "b", "C")

    var answers: MutableList<Int> = MutableList(10) { 0 }

    var score = 0

    val soalViewModel: SoalViewModel by viewModels()

    val tes = "wowkowkwowk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        multiStateView = binding.msvQuestion
        multiStateView.listener = this

        val kategori = intent.getIntExtra(KATEGORI, 0)

        if (kategori == 0) {
            setDataDanKetidakPastianTryOut()
        } else {
            setGeometriDanPengukuranTryOut()
        }

        soalViewModel.currentTimeString.observe(this) {
            binding.countDown.text = String.format(getString(R.string.countdown), it)
        }

        soalViewModel.eventCountDownFinish.observe(this) {
            Toast.makeText(this, "Waktu habis", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityHasil::class.java)
            val completionTimeMillis = soalViewModel.calculateCompletionTime()
            val completionTimeSeconds = completionTimeMillis / 1000
            val completionTimeString = DateUtils.formatElapsedTime(completionTimeSeconds)

            intent.putExtra(ActivityHasil.SCORE, score)
            intent.putExtra(ActivityHasil.ANSWERS, ArrayList(answers))
            intent.putExtra(ActivityHasil.COMPLETION_TIME, completionTimeString)
            startActivity(intent)
        }
    }

    private fun setDataDanKetidakPastianTryOut() {
        soalViewModel.getDataDanKetidakPastianQuestion().observe(this) {
            when (it) {
                is Result.Loading -> multiStateView.viewState = MultiStateView.ViewState.LOADING
                is Result.Success -> {
                    soalViewModel.setInitialTime(1)
                    soalViewModel.setTotalExamTime(1)
                    soalViewModel.startTimer()
                    multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    Log.d("wkwkwk", it.data.toString())
                    val data: ArrayList<QuestionModel> = ArrayList(it.data)
                    answers = MutableList(data.size) { 0 }
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, data, data.size)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.offscreenPageLimit = 10
                    viewPager.isUserInputEnabled = false
                    viewPager.adapter = tabPagerSoalAdapter
                    val tabs: TabLayout = binding.tabs
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = (position + 1).toString()
                    }.attach()


                    for (i in 0..9) {
                        val textView =
                            LayoutInflater.from(this).inflate(R.layout.tab_title, null) as TextView

                        binding.tabs.getTabAt(i)?.customView = textView
                    }
                }

                is Result.Error -> multiStateView.viewState = MultiStateView.ViewState.ERROR
            }
        }
    }


    private fun setGeometriDanPengukuranTryOut() {
        soalViewModel.getGeometriDanPengukuranQuestion().observe(this) {
            when (it) {
                is Result.Loading -> multiStateView.viewState = MultiStateView.ViewState.LOADING
                is Result.Success -> {
                    multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    Log.d("wkwkwk", it.data.toString())
                    val data: ArrayList<QuestionModel> = ArrayList(it.data)
                    answers = MutableList(data.size) { 0 }
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, data, data.size)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.isUserInputEnabled = false
                    viewPager.adapter = tabPagerSoalAdapter
                    val tabs: TabLayout = binding.tabs
                    TabLayoutMediator(tabs, viewPager) { tab, position ->
                        tab.text = (position + 1).toString()
                    }.attach()



                    for (i in 0..9) {
                        val textView =
                            LayoutInflater.from(this).inflate(R.layout.tab_title, null) as TextView
                        binding.tabs.getTabAt(i)?.customView = textView
                    }
                }

                is Result.Error -> multiStateView.viewState = MultiStateView.ViewState.ERROR
            }
        }
    }

    companion object{
        const val KATEGORI = "extra_kategori"
    }

    override fun onStateChanged(viewState: MultiStateView.ViewState) {

    }
}