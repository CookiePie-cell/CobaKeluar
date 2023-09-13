package com.salugan.cobakeluar.ui.activity.hasil

import androidx.lifecycle.ViewModel
import com.salugan.cobakeluar.data.firebase.Repository
import com.salugan.cobakeluar.model.HasilModel
import com.salugan.cobakeluar.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HasilViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){
    val resultHasilTO = repository.resulHasilTO
    fun addHasil(addHasil: HasilModel) =repository.addHasilTryout(addHasil)
}