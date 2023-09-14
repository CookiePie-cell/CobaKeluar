package com.salugan.cobakeluar.ui.activity.materi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.salugan.cobakeluar.databinding.ActivityMateriScreenBinding
import com.salugan.cobakeluar.ui.activity.history.ketidakpastian.ActivityHistory
import com.salugan.cobakeluar.ui.activity.soal.SoalActivity

class MateriScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMateriScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnDataDanKetidakpastian.setOnClickListener {
            val intent = Intent(this@MateriScreenActivity, SoalActivity::class.java)
            intent.putExtra(SoalActivity.KATEGORI, DATA_DAN_KETIDAKPASTIAN)
            startActivity(intent)
        }

        binding.btnGeometriPengukuran.setOnClickListener {
            val intent = Intent(this@MateriScreenActivity, SoalActivity::class.java)
            intent.putExtra(SoalActivity.KATEGORI, GEOMETRI_DAN_PENGUKURAN)
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener({
            startActivity(Intent(this@MateriScreenActivity, ActivityHistory::class.java))

        })

    }

    companion object {
        const val DATA_DAN_KETIDAKPASTIAN = 0
        const val GEOMETRI_DAN_PENGUKURAN = 1
    }
}