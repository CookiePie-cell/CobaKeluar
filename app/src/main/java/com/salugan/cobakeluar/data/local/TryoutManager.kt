package com.salugan.cobakeluar.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TryoutManager(context: Context)  {

    private val dataStore = context.datastore

    val tryoutFinished: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("isFinished")] ?: false
    }

    suspend fun setTryoutFinished(isFinished: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("isFinished")] = isFinished
        }
    }

    companion object {
        private val IS_FINISHED = booleanPreferencesKey("isFinished")
    }
}