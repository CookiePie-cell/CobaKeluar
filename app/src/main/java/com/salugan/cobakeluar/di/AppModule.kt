package com.salugan.cobakeluar.di

import com.salugan.cobakeluar.data.TryoutRepository
import com.salugan.cobakeluar.data.remote.retrofit.ApiConfig
import com.salugan.cobakeluar.data.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()

    @Provides
    @Singleton
    fun provideTryoutRepository(apiService: ApiService): TryoutRepository = TryoutRepository(apiService)
}