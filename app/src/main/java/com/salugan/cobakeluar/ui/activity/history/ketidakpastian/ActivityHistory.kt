package com.salugan.cobakeluar.ui.activity.history.ketidakpastian

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.adapter.HistoryAdapter
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivityHistoryBinding
import com.salugan.cobakeluar.model.HasilModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

//import com.salugan.cobakeluar.adapter.ReportAdapter

@AndroidEntryPoint
class ActivityHistory : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = currentUser?.uid

//        recyclerView = findViewById(R.id.recycleView)
//
//        //  RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
//
//        // Initialize and set up your RecyclerView adapter here
//        val adapter = ReportAdapter()
//        recyclerView.adapter = adapter

        viewModel.dataProfile(id!!)
        viewModel.resultDataProfile.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.namaPengguna.text = result.data.nama
                    binding.emailPengguna.text = result.data.email

//                    val layoutManager = LinearLayoutManager(this)
//                    recyclerView.adapter = adapter
//                    recyclerView.layoutManager = layoutManager
//                    recyclerView.setHasFixedSize(true)
                }

                is Result.Error<*> -> {
                    Toast.makeText(this, "Data gagal diambil", Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                }
            }
        }


        viewModel.getHasilHistory(id).observe(this) {
            when(it) {
                is Result.Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                is Result.Success -> {
                    setAdapter(it.data)
                }
                is Result.Error<*> -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCetak.setOnClickListener {
            val bitmap = getBitmapFromView(binding.halamanCetak)
            saveBitmapToMediaStore(bitmap)
        }
    }

    private fun setAdapter(hasilList: List<HasilModel>) {

        val hasilArrayList = ArrayList(hasilList)
        val adapter = HistoryAdapter(hasilArrayList)
        binding.recycleView.adapter = adapter
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

}