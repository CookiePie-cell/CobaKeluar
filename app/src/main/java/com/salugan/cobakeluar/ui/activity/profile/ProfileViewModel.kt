package com.salugan.cobakeluar.ui.activity.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.data.firebase.Repository
import com.salugan.cobakeluar.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {
    val resultDataProfile = repository.resultDataProfile
    fun dataProfile(id: String): LiveData<Result<UserModel>> {
        return repository.dataProfile(id)
    }
}