package com.salugan.cobakeluar.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityProfileBinding
import com.salugan.cobakeluar.ui.activity.authentication.ActivityLogin

class ActivityProfile: AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    var dialogLogout: AlertDialog? = null
    var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var nama = intent.getStringExtra("nama")?:""
        var email = intent.getStringExtra("email")?: ""
        var phoneNumber = intent.getStringExtra("phoneNumber")?: ""
        var userPhoto = intent.getStringExtra("userPhoto")

        binding.nama.text = nama
        binding.email.text = email
        binding.phoneNumber.text = phoneNumber

        if (userPhoto != null){
            Glide.with(binding.root)
                .load(userPhoto)
                .into(binding.userPhoto)
        }

        binding.btnBack.setOnClickListener(){
            onBackPressed()
        }

        binding.btnLogout.setOnClickListener(){
            dialogLogout()


        }


    }

    fun dialogLogout(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val builder = AlertDialog.Builder(this)

        // Dapatkan referensi ke button btnYa dan btnTidak dari dialogView
        val btnYa = dialogView.findViewById<TextView>(R.id.btnYa)
        val btnTidak = dialogView.findViewById<TextView>(R.id.btnTidak)

        builder.setView(dialogView)
        dialogLogout = builder.create()
        dialogLogout?.show()

        btnYa.setOnClickListener(){
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

        btnTidak.setOnClickListener(){
            onBackPressed()
        }
    }

    /**
     * this method to handle loading for  action waiting to signin.
     * @author Faiz Ivan Tama
     * @since Sept 2023.
     * */
    private fun loading() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        loadingDialog = builder.create()
        loadingDialog?.show()
    }


}