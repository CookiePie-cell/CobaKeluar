package com.salugan.cobakeluar.ui.activity.authentication.signup

import androidx.lifecycle.ViewModel
import com.salugan.cobakeluar.data.firebase.Repository
import com.salugan.cobakeluar.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){
    val resultAddData = repository.resultAddData
    fun addUser(addData: UserModel) =repository.userData(addData)
}