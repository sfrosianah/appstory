package com.dicoding.syamsustoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.syamsustoryapp.data.model.*
import com.dicoding.syamsustoryapp.retrofit.ApiService
import com.dicoding.syamsustoryapp.data.session.LoginPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val pref: LoginPreferences, private val apiService: ApiService) {

    private var currentPagingSource: StoryPagingSource? = null

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        runCatching {
            val response = apiService.postLogin(email, password)
            emit(if (response.error) Result.Error(response.message) else Result.Success(response))
        }.onFailure { e -> emit(Result.Error(e.message ?: "Unknown Error")) }
    }

    fun postRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        runCatching {
            val response = apiService.postRegister(name, email, password)
            emit(if (response.error) Result.Error(response.message) else Result.Success(response))
        }.onFailure { e -> emit(Result.Error(e.message ?: "Unknown Error")) }
    }

    fun getListStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(pref, apiService).also { currentPagingSource = it }
            }
        ).liveData
    }

    fun refreshListStory() {
        currentPagingSource?.invalidate()
    }

    fun getStoryWithMap(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        runCatching {
            val token = "Bearer ${pref.loginData.token}"
            val response = apiService.getStory(token, page = 1, size = 100, location = 1)
            emit(if (response.error) Result.Error(response.message) else Result.Success(response))
        }.onFailure { e -> emit(Result.Error(e.message ?: "Unknown Error")) }
    }

    fun uploadImage(imageFile: MultipartBody.Part, desc: RequestBody, lat: RequestBody?, lon: RequestBody?): LiveData<Result<AddNewStoryResponse>> = liveData {
        emit(Result.Loading)
        runCatching {
            val token = "Bearer ${pref.loginData.token}"
            val response = apiService.uploadImage(token, imageFile, desc, lat, lon)
            emit(if (response.error) Result.Error(response.message) else Result.Success(response))
        }.onFailure { e -> emit(Result.Error(e.message ?: "Unknown Error")) }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(pref: LoginPreferences, apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(pref, apiService).also { instance = it }
            }
    }
}
