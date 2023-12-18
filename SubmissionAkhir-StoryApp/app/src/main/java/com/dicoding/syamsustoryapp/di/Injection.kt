package com.dicoding.syamsustoryapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.syamsustoryapp.data.StoryRepository
import com.dicoding.syamsustoryapp.retrofit.ApiConfig
import com.dicoding.syamsustoryapp.retrofit.ApiService
import com.dicoding.syamsustoryapp.data.session.LoginPreferences

val Context.dataStore by preferencesDataStore(name = "story")

object Injection {
    private fun provideApiService(): ApiService = ApiConfig.getApiService()

    private fun providePreferences(context: Context): LoginPreferences = LoginPreferences(context)

    fun provideRepository(context: Context): StoryRepository {
        val preferences = providePreferences(context)
        val apiService = provideApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}
