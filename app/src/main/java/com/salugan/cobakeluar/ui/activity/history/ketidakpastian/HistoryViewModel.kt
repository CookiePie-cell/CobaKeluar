package com.salugan.cobakeluar.ui.activity.history.ketidakpastian

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.data.firebase.Repository
import com.salugan.cobakeluar.model.HasilModel
import com.salugan.cobakeluar.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    val resultDataProfile = repository.resultDataProfile

    fun dataProfile(id: String): LiveData<Result<UserModel>> {
        return repository.dataProfile(id)
    }

    fun getHasilHistory(id: String): LiveData<Result<List<HasilModel>>> {
        return repository.getHasilTryout(id)
    }
}