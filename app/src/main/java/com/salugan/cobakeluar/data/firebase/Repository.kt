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

class Repository @Inject constructor(
    private val db: FirebaseDatabase
){
    val resultAddData = MutableLiveData<Result<String>>()
    val resultDataProfile = MutableLiveData<Result<UserModel>>()
    fun userData(addData: UserModel): LiveData<Result<String>> {
        val database = db.getReference("users").push()
        database.setValue(addData)
        resultAddData.postValue(Result.Loading)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultAddData.postValue(Result.Success("Data berhasil ditambahkan"))
            }
            override fun onCancelled(error: DatabaseError) {
                resultAddData.postValue(Result.Error(error.message))
            }
        })
        return resultAddData
    }
    fun dataProfile(id: String): LiveData<Result<UserModel>> {
        val database =db.getReference("users")
        val query = database.orderByChild("id").equalTo(id)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foundUser = mutableListOf<UserModel>()
                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(UserModel::class.java)
                    user?.let { foundUser.add(it) }
                }
                if (foundUser.isNotEmpty()) {
                    resultDataProfile.value = Result.Success(foundUser[0])
                } else {
                    resultDataProfile.value = Result.Error("User tidak ditemukan")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                resultDataProfile.value = Result.Error(error.message)
            }
        })
        return resultDataProfile
    }
}