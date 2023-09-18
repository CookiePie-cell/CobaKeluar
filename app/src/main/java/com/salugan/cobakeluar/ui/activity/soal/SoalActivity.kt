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
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kennyc.view.MultiStateView
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.TabPagerSoalAdapter
import com.salugan.cobakeluar.data.Error
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivitySoalBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.ui.activity.hasil.ActivityHasil
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

@AndroidEntryPoint
class SoalActivity : AppCompatActivity(), MultiStateView.StateListener {

    private lateinit var binding: ActivitySoalBinding

    private lateinit var multiStateView: MultiStateView

    var answers: MutableList<Int> = MutableList(10) { 0 }

    var score = 0

    val soalViewModel: SoalViewModel by viewModels()

    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        multiStateView = binding.msvQuestion
        multiStateView.listener = this

        val kategori = intent.getIntExtra(KATEGORI, 0)


        soalViewModel.setTryoutStatus(false)

        if (kategori == 0) {
            setDataDanKetidakPastianTryOut()
        } else {
            setGeometriDanPengukuranTryOut()
        }

        tvError = multiStateView.findViewById(R.id.tvError)

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
                    soalViewModel.getTryoutStatus().observe(this) {
                        Log.d("itumasbrow", it.toString())
                    }
                    multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    startTimer()
                    val data: ArrayList<QuestionModel> = ArrayList(it.data)
                    answers = MutableList(data.size) { 0 }
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, data, data.size)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.offscreenPageLimit = data.size
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

                is Result.Error<*> -> {
                    multiStateView.viewState = MultiStateView.ViewState.ERROR
                    val errorData = it.errorData as Error
                    tvError.text = errorData.message
                    Snackbar.make(
                        window.decorView.rootView,
                        errorData.message,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("RETRY") {
                        setDataDanKetidakPastianTryOut()
                    }.show()
                }
            }
        }
    }


    private fun setGeometriDanPengukuranTryOut() {
        soalViewModel.getGeometriDanPengukuranQuestion().observe(this) {
            when (it) {
                is Result.Loading -> multiStateView.viewState = MultiStateView.ViewState.LOADING
                is Result.Success -> {
                    multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    startTimer()
                    val data: ArrayList<QuestionModel> = ArrayList(it.data)
                    answers = MutableList(data.size) { 0 }
                    val tabPagerSoalAdapter = TabPagerSoalAdapter(this, data, data.size)
                    val viewPager: ViewPager2 = binding.viewPager
                    viewPager.offscreenPageLimit = data.size
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

                is Result.Error<*> -> {
                    multiStateView.viewState = MultiStateView.ViewState.ERROR
                    val errorData = it.errorData as Error
                    tvError.text = errorData.message
                    Snackbar.make(
                        window.decorView.rootView,
                        errorData.message,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("RETRY") {
                        setGeometriDanPengukuranTryOut()
                    }.show()
                }
            }
        }
    }


    private fun startTimer() {
        soalViewModel.setInitialTime(30)
        soalViewModel.setTotalExamTime(30)
        soalViewModel.startTimer()
    }

    companion object{
        const val KATEGORI = "extra_kategori"
    }

    override fun onStateChanged(viewState: MultiStateView.ViewState) {

    }
}