//package com.dicoding.syamsustoryapp.data.model
//
//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
//
//@Parcelize
//data class LoginResponse(
//    val message: String,
//    val error: Boolean,
//    val loginResult: LoginResult? = null
//) : Parcelable
//
//@Parcelize
//data class LoginResult(
//    val userId: String,
//    val name: String,
//    val token: String
//) : Parcelable

package com.dicoding.syamsustoryapp.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class LoginResult(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String
) : Parcelable
