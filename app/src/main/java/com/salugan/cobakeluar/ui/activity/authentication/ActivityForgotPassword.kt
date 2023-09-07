package com.salugan.cobakeluar.ui.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityForgotPasswordBinding

class ActivityForgotPassword: AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth

    var dialogForgotPassword: AlertDialog? = null
    var loadingDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()


        binding.btnKirim.setOnClickListener({
            val email = binding.textEmailLupaPass.text.toString()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                Toast.makeText(this, "Masukan email yang terdaftar", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun resetPassword(email: String) {
        loading()
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingDialog?.dismiss()
                    dialogForgotPassword()
                } else {loadingDialog?.dismiss()
                    Toast.makeText(this, "Gagal mengirim email reset password. Periksa kembali alamat email Anda.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun dialogForgotPassword(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val builder = AlertDialog.Builder(this)

        val btnOk = dialogView.findViewById<TextView>(R.id.btnOk)

        builder.setView(dialogView)
        dialogForgotPassword = builder.create()
        dialogForgotPassword?.show()

        btnOk.setOnClickListener(){
            finish()
            onBackPressed()
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