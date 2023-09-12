package com.salugan.cobakeluar.ui.activity.hasil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivityHasilToBinding
import com.salugan.cobakeluar.model.HasilModel
import com.salugan.cobakeluar.model.UserModel
import com.salugan.cobakeluar.ui.activity.authentication.signup.SignupViewModel
import com.salugan.cobakeluar.ui.activity.home.HomeActivity
import com.salugan.cobakeluar.utils.DateTimeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityHasil : AppCompatActivity() {
    private lateinit var binding: ActivityHasilToBinding
    private lateinit var viewModel: HasilViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilToBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(HasilViewModel::class.java)


        val score = intent.getIntExtra(SCORE, 0)
        val answers = intent.getIntegerArrayListExtra(ANSWERS)
        val completionTime = intent.getStringExtra(COMPLETION_TIME)

        val nilai = (score * 10).toString()
        val tanggal = DateTimeUtils.getCurrentDate()
        val waktu = completionTime

        binding.nilai.text = nilai
        binding.tanggal.text = tanggal
        binding.waktu.text = waktu

        if (answers != null) {

            val benar = String.format(getString(R.string.benar), answers.count { it == 1 })
            val salah = String.format(getString(R.string.salah), answers.count { it == 2 })
            val kosong = String.format(getString(R.string.kosong), answers.count { it == 0 })

            val jumlahBenar = answers.count { it == 1 }
            val jumlahSalah = answers.count { it == 2 }
            val jumlahKosong = answers.count { it == 0 }

            binding.benar.text = benar
            binding.salah.text = salah
            binding.kosong.text = kosong

            val mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            val id = currentUser?.uid


            val addHasil = HasilModel(
                id = id,
                nilai = nilai,
                tanggal = tanggal,
                waktu = waktu,
                benar = jumlahBenar,
                salah = jumlahSalah,
                kosong = jumlahKosong
            )

            viewModel.addHasil(addHasil)
            viewModel.resultHasilTO.observe(this) {
                when (it) {
                    is Result.Loading -> {
                    }

                    is Result.Success -> {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is Result.Error -> {
                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        const val SCORE = "extra_score"
        const val ANSWERS = "extra_answers"
        const val COMPLETION_TIME = "extra_completion_time"
    }
}