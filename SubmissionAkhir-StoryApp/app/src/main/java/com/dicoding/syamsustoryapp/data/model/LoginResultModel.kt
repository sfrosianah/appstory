package com.dicoding.syamsustoryapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResultModel(
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null
) : Parcelable