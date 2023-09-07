package com.salugan.cobakeluar.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.salugan.cobakeluar.model.UserModel
import javax.inject.Inject
import com.salugan.cobakeluar.data.Result
import okhttp3.internal.http.hasBody

class Repository @Inject constructor(
    private val db: FirebaseDatabase
){
    val resultAddData = MutableLiveData<Result<String>>()

    fun userData(addData: UserModel): LiveData<Result<String>> {
        val database = db.getReference("users").push()

        database.setValue(addData)

        resultAddData.postValue(Result.Loading)


        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultAddData.postValue(Result.Success("Data berhasil ditambahkan"))
                Log.i("suksessBos", "suskes: " )

            }

            override fun onCancelled(error: DatabaseError) {
                resultAddData.postValue(Result.Error(error.message))
                Log.e("errorDatabaseBos", "onCancelled: "+ error.message )
            }
        })

        return resultAddData
    }
}