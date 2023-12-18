package com.dicoding.syamsustoryapp.retrofit

import com.dicoding.syamsustoryapp.data.model.*
import com.dicoding.syamsustoryapp.data.model.AddNewStoryResponse
import com.dicoding.syamsustoryapp.data.model.StoryResponse
import com.dicoding.syamsustoryapp.data.model.LoginResponse
import com.dicoding.syamsustoryapp.data.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): AddNewStoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): Response <StoryResponse>

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): StoryResponse
}