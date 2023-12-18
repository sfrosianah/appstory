package com.dicoding.syamsustoryapp.data.session

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Response(

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class Datas(

	@field:SerializedName("token")
	val token: String? = null
) : Parcelable

@Parcelize
data class Payload(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("datas")
	val datas: Datas? = null
) : Parcelable
