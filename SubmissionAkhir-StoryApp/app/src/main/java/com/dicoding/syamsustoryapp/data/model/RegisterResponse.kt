package com.dicoding.syamsustoryapp.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable
