package com.salugan.cobakeluar.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityProfileBinding
import com.salugan.cobakeluar.model.UserModel
import com.salugan.cobakeluar.ui.activity.authentication.ActivityLogin
import com.salugan.cobakeluar.data.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityProfile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    var dialogLogout: AlertDialog? = null
    var loadingDialog: AlertDialog? = null
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = currentUser?.uid
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.dataProfile(id!!)
        viewModel.resultDataProfile.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    loadingDialog?.dismiss()
                    binding.nama.text = result.data.nama
                    binding.email.text = result.data.email
                    binding.phoneNumber.text = result.data.noHp
                }
                is Result.Error<*> -> {
                    loadingDialog?.dismiss()
                    Toast.makeText(this, "Data gagal diambil", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    loading()
                }
            }
        })
        binding.btnLogout.setOnClickListener() {
            dialogLogout()
        }
    }
    fun dialogLogout() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val builder = AlertDialog.Builder(this)
        // Dapatkan referensi ke button btnYa dan btnTidak dari dialogView
        val btnYa = dialogView.findViewById<TextView>(R.id.btnYa)
        val btnTidak = dialogView.findViewById<TextView>(R.id.btnTidak)
        builder.setView(dialogView)
        dialogLogout = builder.create()
        dialogLogout?.show()
        btnYa.setOnClickListener() {
            loading()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener(this) {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
                finish()
                loadingDialog?.dismiss()
            }
        }
        btnTidak.setOnClickListener() {
            dialogLogout?.dismiss()
        }
    }
    private fun loading() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        loadingDialog = builder.create()
        loadingDialog?.show()
    }
}