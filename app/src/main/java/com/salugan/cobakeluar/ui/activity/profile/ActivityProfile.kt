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
import com.kennyc.view.MultiStateView
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityProfileBinding
import com.salugan.cobakeluar.ui.activity.authentication.ActivityLogin
import com.salugan.cobakeluar.data.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityProfile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    var dialogLogout: AlertDialog? = null
    var loadingDialog: AlertDialog? = null
    private lateinit var viewModel: ProfileViewModel
    private lateinit var multiStateView: MultiStateView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        multiStateView = binding.dataProfile

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = currentUser?.uid
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.dataProfile(id!!)
        viewModel.resultDataProfile.observe(this) {
            when (it) {
                is Result.Success -> {
                    if(it.data.email!!.isEmpty() && it.data.nama!!.isEmpty() && it.data.noHp!!.isEmpty()){
                        multiStateView.viewState = MultiStateView.ViewState.EMPTY
                    }
                    multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    binding.nama.text = it.data.nama
                    binding.email.text = it.data.email
                    binding.phoneNumber.text = it.data.noHp
                }
                is Result.Error<*> -> {
                    multiStateView.viewState = MultiStateView.ViewState.ERROR
                    loadingDialog?.dismiss()
                    Toast.makeText(this, "Data gagal diambil", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    multiStateView.viewState = MultiStateView.ViewState.LOADING
                }
            }
        }
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
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
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