package com.salugan.cobakeluar.ui.activity.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.databinding.ActivitySignUpBinding
import com.salugan.cobakeluar.model.UserModel
import com.salugan.cobakeluar.ui.activity.home.HomeActivity
import com.salugan.cobakeluar.data.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitySignUp: AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        var nama = intent.getStringExtra("nama")?:""
        var email = intent.getStringExtra("email")?: ""

        binding.nama.text = Editable.Factory.getInstance().newEditable(nama)
        binding.email.text = Editable.Factory.getInstance().newEditable(email)

        binding.btnDaftar.setOnClickListener({
            val nama = binding.nama.text.toString()
            val email = binding.email.text.toString()
            val noHp = binding.noHp.text.toString()
            val password = binding.Password.text.toString()
            val konfirmPassword = binding.konfirmPassword.text.toString()

            if (password != konfirmPassword){
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show()
            }else if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty() || password.isEmpty() || konfirmPassword.isEmpty() ){
                Toast.makeText(this, "Data harus terisi semua", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = FirebaseAuth.getInstance().currentUser
                            val uid = firebaseUser?.uid

                            val addData = UserModel(
                                id = uid!!,
                                nama = nama,
                                email = email,
                                noHp  = noHp
                            )

                            viewModel.addUser(addData)

                            viewModel.resultAddData.observe(this) {
                                when (it) {
                                    is Result.Loading -> {
                                        // Handle loading state if needed
                                    }

                                    is Result.Success -> {
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                    }

                                    is Result.Error -> {
                                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                                    }

                        }}}
                        else{
                            Toast.makeText(this, "gagal :"+task.exception, Toast.LENGTH_SHORT).show()
                            Log.e("errorFirebase", "onCreate: ", task.exception )
                        }
                        }
            }
        })
    }
}