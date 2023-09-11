package com.salugan.cobakeluar.ui.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.ActivityLoginBinding
import com.salugan.cobakeluar.ui.activity.authentication.signup.ActivitySignUp
import com.salugan.cobakeluar.ui.activity.home.HomeActivity

class ActivityLogin: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var mAuth: FirebaseAuth
    var loadingDialog: AlertDialog? = null
    // Request code for Google Sign-In
    private val requestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()

        binding.btnMasuk.setOnClickListener({
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Email dan password harus di isi", Toast.LENGTH_SHORT).show()
            }
        })

//        binding.btnDaftar.setOnClickListener({
//            val intent = Intent(this, ActivitySignUp::class.java)
//            startActivity(intent)
//        })

        binding.btnLoginGoogle.setOnClickListener({
            signIn()
        })

        binding.btnTextLupaPassword.setOnClickListener({
            forgotPassword()
        })

    }

    private fun loginUser(email: String, password: String) {
        loading()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loadingDialog?.dismiss()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                } else {
                    loadingDialog?.dismiss()
                    Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, requestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            try {

                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "gagal: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        loading()

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val isNewUser = task.result?.additionalUserInfo?.isNewUser
                    if (isNewUser == true) {
                        mAuth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                var nama = user?.displayName
                                var email = user?.email

                                loadingDialog?.dismiss()

                                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .build()
                                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                                googleSignInClient.signOut().addOnCompleteListener(this) {
                                    FirebaseAuth.getInstance().signOut()

                                    val intent = Intent(this, ActivitySignUp::class.java)
                                    intent.putExtra("nama", nama)
                                    intent.putExtra("email", email)
                                    startActivity(intent)

                                    Toast.makeText(this, "Anda belum memiliki akun", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                loadingDialog?.dismiss()
                                Toast.makeText(this, "Gagal membuat akun baru", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    } else {
                        loadingDialog?.dismiss()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    loadingDialog?.dismiss()
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun forgotPassword() {
        val intent = Intent(this, ActivityForgotPassword::class.java)
        startActivity(intent)
    }

    private fun loading() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        loadingDialog = builder.create()
        loadingDialog?.show()
    }

}

