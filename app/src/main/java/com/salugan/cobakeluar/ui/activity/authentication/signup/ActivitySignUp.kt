package com.salugan.cobakeluar.ui.activity.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivitySignUpBinding
import com.salugan.cobakeluar.model.UserModel
import com.salugan.cobakeluar.ui.activity.home.HomeActivity
import com.salugan.cobakeluar.data.Result
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class ActivitySignUp: AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignupViewModel
    var loadingDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        val nama = intent.getStringExtra("nama")?:""
        val email = intent.getStringExtra("email")?: ""
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
            }else if(!isValidPhoneNumber(noHp)){
                Toast.makeText(this, "Nomor telepon tidak valid", Toast.LENGTH_SHORT).show()
            }
            else{
                loading()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = FirebaseAuth.getInstance().currentUser
                            val uid = firebaseUser?.uid
                            val addData = UserModel(
                                id = uid!!,
                                nama = nama,
                                email = email,
                                noHp  = noHp,
                            )
                            viewModel.addUser(addData)
                            viewModel.resultAddData.observe(this) {
                                when (it) {
                                    is Result.Loading -> {
                                        loading()
                                    }
                                    is Result.Success -> {
                                        loadingDialog?.dismiss()
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                    }
                                    is Result.Error -> {
                                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .build()
                                        val googleSignInClient = GoogleSignIn.getClient(this, gso)
                                        googleSignInClient.signOut().addOnCompleteListener(this) {
                                            FirebaseAuth.getInstance().signOut()}
                                        loadingDialog?.dismiss()
                                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                                    }
                        }}}
                        else{
                            loadingDialog?.dismiss()
                            Toast.makeText(this, "gagal :"+task.exception, Toast.LENGTH_SHORT).show()
                        }
                        }
            }
        })
       }

    private fun isValidPhoneNumber(noHP: String): Boolean {
        val pattern = Pattern.compile("^[0-9]{12}\$|^[0-9]{11}\$|^[0-9]{13}\$")
        val matcher = pattern.matcher(noHP)
        return matcher.matches()
    }
    private fun loading() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        loadingDialog = builder.create()
        loadingDialog?.show()
    }
}