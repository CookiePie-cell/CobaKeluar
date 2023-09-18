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

        initView()

    }

    /**
     * This function is used to initialize the view and display the quiz result details on the screen.
     * It retrieves the quiz score, answers, completion time, and other details from the intent extras.
     * It also calculates and displays the user's score, date, and completion time.
     * Additionally, it handles user interaction with the "Home" button and records the quiz result to Firebase.
     *
     * @author [Faiz Ivan Tama]
     * @since September 2023.
     * @see [Firebase Authentication documentation](https://firebase.google.com/docs/auth)
     * @see [Firebase Realtime Database documentation](https://firebase.google.com/docs/database)
     * @see [Date and Time in Android](https://developer.android.com/reference/java/time/package-summary)
     * @param answers An [ArrayList] containing user's answers to quiz questions.
     * @param completionTime The completion time of the quiz.
     */
    private fun initView(){

        val score = intent.getIntExtra(SCORE, 0)
        val answers = intent.getIntegerArrayListExtra(ANSWERS)
        val completionTime = intent.getStringExtra(COMPLETION_TIME)

        val nilai = (score * 10).toString()
        val tanggal = DateTimeUtils.getCurrentDate()
        val waktu = completionTime


        binding.nilai.text = nilai
        binding.tanggal.text = tanggal
        binding.waktu.text = waktu

        binding.btnHome.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

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
                id = null,
                userId = id,
                nilai = nilai,
                tanggal = tanggal,
                waktu = waktu,
                benar = jumlahBenar,
                salah = jumlahSalah,
                kosong = jumlahKosong
            )

            viewModel.getTryoutStatus().observe(this) {
                if (!it) {
                    viewModel.addHasil(addHasil)
                    viewModel.setTryoutStatus(true)
                }
            }
            viewModel.resultHasilTO.observe(this) {
                when (it) {
                    is Result.Loading -> {
                    }

                    is Result.Success -> {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is Result.Error<*> -> {
                        Toast.makeText(this, it.errorData as String, Toast.LENGTH_SHORT).show()
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