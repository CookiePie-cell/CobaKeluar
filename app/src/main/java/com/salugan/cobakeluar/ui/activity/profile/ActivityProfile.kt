package com.salugan.cobakeluar.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.databinding.ActivityProfileBinding
import com.salugan.cobakeluar.ui.activity.authentication.ActivityLogin

class ActivityProfile: AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

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
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener(this) {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            }

        }


    }

}