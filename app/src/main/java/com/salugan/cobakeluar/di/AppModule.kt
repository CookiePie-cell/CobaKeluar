package com.salugan.cobakeluar.di

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.salugan.cobakeluar.data.TryoutRepository
import com.salugan.cobakeluar.data.firebase.Repository
import com.salugan.cobakeluar.data.local.TryoutManager
import com.salugan.cobakeluar.data.remote.retrofit.ApiConfig
import com.salugan.cobakeluar.data.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideRepository(db: FirebaseDatabase, @ApplicationContext context: Context): Repository = Repository(db, context)


    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()

    @Provides
    @Singleton
    fun provideTryoutRepository(apiService: ApiService): TryoutRepository = TryoutRepository(apiService)

    @Provides
    @Singleton
    fun provideTryoutManager(@ApplicationContext context: Context): TryoutManager = TryoutManager(context)
}