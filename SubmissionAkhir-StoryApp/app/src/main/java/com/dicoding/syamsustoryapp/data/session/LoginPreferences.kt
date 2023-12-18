package com.dicoding.syamsustoryapp.data.session

import android.content.Context
import com.dicoding.syamsustoryapp.data.model.LoginResultModel

class LoginPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE)

    var loginData: LoginResultModel
        get() = LoginResultModel(
            userId = preferences.getString(USER_ID, null),
            name = preferences.getString(NAME, null),
            token = preferences.getString(TOKEN, null)
        )
        set(value) = with(preferences.edit()) {
            putString(NAME, value.name)
            putString(USER_ID, value.userId)
            putString(TOKEN, value.token)
            apply()
        }

    fun deleteUser() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val NAME_PREF = "login_pref"
        private const val NAME = "extra_name"
        private const val USER_ID = "extra_userId"
        private const val TOKEN = "extra_token"
    }
}
