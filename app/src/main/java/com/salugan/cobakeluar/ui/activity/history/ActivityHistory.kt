package com.salugan.cobakeluar.ui.activity.history

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivityHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ActivityHistory : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = currentUser?.uid

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.dataProfile(id!!)

        viewModel.resultDataProfile.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    binding.namaPengguna.text = result.data.nama
                    binding.emailPengguna.text = result.data.email
                }

                is Result.Error -> {
                    Toast.makeText(this, "Data gagal diambil", Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                }
            }
        })


        binding.btnCetak.setOnClickListener {
            val bitmap = getBitmapFromView(binding.HalamanCetak)
            saveBitmapToMediaStore(bitmap)
        }

        binding.btnCetakPdf.setOnClickListener({
            val bitmap = getBitmapFromView(binding.HalamanCetak)
            val pdfFileName = "hasil-nilai.pdf"

            createPdfFromBitmap(bitmap, pdfFileName)

        })
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

    private fun createPdfFromBitmap(bitmap: Bitmap, pdfFileName: String) {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        storageDir?.mkdirs()
        val pdfFile = File(storageDir, pdfFileName)

        val pdfWriter = PdfWriter(pdfFile)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val image = Image(ImageDataFactory.create(bitmapToByteArray(bitmap)))
        image.scaleToFit(pdfDocument.defaultPageSize.width, pdfDocument.defaultPageSize.height)

        document.add(image)

        document.close()
        pdfWriter.close()

        Log.d("PDF Creation", "PDF File Path: ${pdfFile.absolutePath}")

        Toast.makeText(this, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show()
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
        val score = intent.getIntExtra(SCORE, 0)

        binding.nilaiData.text = score.toString()
    }

    companion object {
        const val SCORE = "extra_score"
    }

}