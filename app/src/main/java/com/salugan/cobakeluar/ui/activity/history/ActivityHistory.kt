package com.salugan.cobakeluar.ui.activity.history

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivityHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import com.salugan.cobakeluar.R
//import com.salugan.cobakeluar.adapter.ReportAdapter

@AndroidEntryPoint
class ActivityHistory : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = currentUser?.uid

        val score = intent.getIntExtra(SCORE, 0)

//        recyclerView = findViewById(R.id.recycleView)
//
//        //  RecyclerView
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//
//        // Initialize and set up your RecyclerView adapter here
//        val adapter = ReportAdapter()
//        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.dataProfile(id!!)

        viewModel.resultDataProfile.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    binding.namaPengguna.text = result.data.nama
                    binding.emailPengguna.text = result.data.email

//                    val layoutManager = LinearLayoutManager(this)
//                    recyclerView.adapter = adapter
//                    recyclerView.layoutManager = layoutManager
//                    recyclerView.setHasFixedSize(true)
                }

                is Result.Error -> {
                    Toast.makeText(this, "Data gagal diambil", Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                }
            }
        })


        binding.btnCetak.setOnClickListener {
            val bitmap = getBitmapFromView(binding.halamanCetak)
            saveBitmapToMediaStore(bitmap)
        }

    }

    private fun getBitmapFromView(view: View): Bitmap {
        return view.drawToBitmap()
    }

    private fun saveBitmapToMediaStore(bitmap: Bitmap) {
        val resolver = contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "hasil-nilai.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val output = resolver.openOutputStream(imageUri!!)

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            output?.close()
            Toast.makeText(this, "Berhasil mengunduh, perikasi galeri anda", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal mengunduh", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val SCORE = "extra_score"
    }

}