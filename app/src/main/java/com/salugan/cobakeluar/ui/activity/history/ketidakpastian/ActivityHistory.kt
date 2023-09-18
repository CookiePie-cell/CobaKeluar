package com.salugan.cobakeluar.ui.activity.history.ketidakpastian

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kennyc.view.MultiStateView
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.HistoryAdapter
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.databinding.ActivityHistoryBinding
import com.salugan.cobakeluar.model.HasilModel
import com.salugan.cobakeluar.utils.DeviceConnection
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ActivityHistory : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var multiStateView: MultiStateView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        multiStateView = binding.listHistori

        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager


        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = currentUser?.uid

        showHistoryTryout(id!!)

        binding.btnCetak.setOnClickListener {
            val bitmap = getBitmapFromView(binding.halamanCetak)
            saveBitmapToMediaStore(bitmap)
        }
    }

    /**
     * This function is used to retrieve and display the history of a user's tryouts based on their ID.
     * It uses the ViewModel to fetch the tryout history data and updates the UI accordingly.
     * @author [Julio Nicholas]
     * @since September 2023.
     * @param id The user's ID for whom the tryout history is to be displayed.
     * @see [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
     * @see [MultiStateView](https://github.com/Kennyc1012/MultiStateView)
     */
    private fun showHistoryTryout(id: String) {
        viewModel.getHasilHistory(id).observe(this) {
            when (it) {
                is Result.Loading -> multiStateView.viewState = MultiStateView.ViewState.LOADING
                is Result.Success -> {
                    if (it.data.isEmpty()) {
                        multiStateView.viewState = MultiStateView.ViewState.EMPTY
                    } else {
                        multiStateView.viewState = MultiStateView.ViewState.CONTENT
                        setAdapter(it.data)
                    }
                }

                is Result.Error<*> -> {
                    multiStateView.viewState = MultiStateView.ViewState.ERROR
                    val tvError: TextView = multiStateView.findViewById(R.id.tvError)
                    tvError.text = it.errorData.toString()
                    Snackbar.make(
                        window.decorView.rootView,
                        it.errorData.toString(),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("RETRY") {
                        showHistoryTryout(id)
                    }.show()
                }
            }
        }


    }

    /**
     * This function is used to set up and attach an adapter to a RecyclerView to display a list of quiz results.
     * @author [Julio Nicholas]
     * @since September 2023.
     * @param hasilList The list of quiz results to be displayed in the RecyclerView.
     * @see [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
     * @see [HistoryAdapter](link/to/HistoryAdapter)
     */
    private fun setAdapter(hasilList: List<HasilModel>) {

        val hasilArrayList = ArrayList(hasilList)
        val adapter = HistoryAdapter(hasilArrayList)
        binding.recycleView.adapter = adapter
    }

    private fun getBitmapFromView(view: View): Bitmap {
        return view.drawToBitmap()
    }

    /**
     * This function is used to save a bitmap image to the device's media store, making it available in the gallery.
     * It creates an entry for the image in the media store and writes the bitmap data to it.
     * @author [Faiz Ivan Tama]
     * @since September 2023.
     * @param bitmap The bitmap image to be saved to the media store.
     * @see [ContentResolver](https://developer.android.com/reference/android/content/ContentResolver)
     * @see [MediaStore.Images.Media](https://developer.android.com/reference/android/provider/MediaStore.Images.Media)
     * @see [Bitmap](https://developer.android.com/reference/android/graphics/Bitmap)
     */
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