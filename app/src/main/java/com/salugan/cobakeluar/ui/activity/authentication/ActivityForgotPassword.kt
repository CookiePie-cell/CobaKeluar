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
        binding.btnKirim.setOnClickListener {
            validateEmail()
        }
    }

    /**
     * This function is responsible for validating and processing a password reset request based on the provided email.
     * If the email is not empty, it initiates the password reset process using the provided email address. If the email
     * is empty, it displays a toast message to prompt the user to enter a registered email address.
     * @author [Faiz Ivan Tama]
     * @since September 2023.
     * @see resetPassword
     */
    private fun validateEmail(){
        val email = binding.textEmailLupaPass.text.toString()
        if (email.isNotEmpty()) {
            resetPassword(email)
        } else {
            Toast.makeText(this, "Masukan email yang terdaftar", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * This function initiates the password reset process by sending a password reset email to the provided email address.
     * It displays a loading indicator while sending the email. If the email is sent successfully, it displays a dialog
     * to inform the user about the password reset email. If there is an error sending the email, it shows a toast message
     * with an error message.
     * @author [Faiz Ivan Tama]
     * @since September 2023.
     * @param email The email address to which the password reset email will be sent.
     * @see dialogForgotPassword
     */
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

    /**
     * This function displays a dialog to inform the user that a password reset email has been sent successfully.
     * It provides a button (Ok) that allows the user to close the dialog and return to the previous screen.
     * @author [Faiz Ivan Tama]
     * @since September 2023.
     */
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

    /**
     * Handle process is loading
     * @author [Faiz Ivan Tama]
     * @since September 2023.
     * */
    private fun loading() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        loadingDialog = builder.create()
        loadingDialog?.show()
    }
}