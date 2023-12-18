package com.dicoding.syamsustoryapp.utils

import com.dicoding.syamsustoryapp.data.model.AddNewStoryResponse
import com.dicoding.syamsustoryapp.data.model.StoryResponse
import com.dicoding.syamsustoryapp.data.model.ListStoryItem
import com.dicoding.syamsustoryapp.data.model.LoginResponse
import com.dicoding.syamsustoryapp.data.model.LoginResult
import com.dicoding.syamsustoryapp.data.model.RegisterResponse

object DataDummy {
    fun generateDummySuccesLogin(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                userId = "userId",
                name = "name",
                token = "token"
            )
        )
    }
    fun generateDummyErrorLogin(): LoginResponse {
        return LoginResponse(
            error = true,
            message = "invalid password"
        )
    }

    fun generateDummySuccesRegister(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyErrorResgister(): RegisterResponse {
        return RegisterResponse(
            error = true,
            message = "Email is already taken"
        )
    }

    fun generateDummyDataStory(): StoryResponse {
        return StoryResponse(
            error = false,
            message = "success",
            listStory = arrayListOf(
                ListStoryItem(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.12,
                    lon = 0.11
                )
            )
        )
    }

    fun generateDummyErrorDataStory(): StoryResponse {
        return StoryResponse(
            error = true,
            message = "error",
            listStory = arrayListOf(
                ListStoryItem(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.12,
                    lon = 0.11
                )
            )
        )
    }

    fun generateDummySuccesUpload(): AddNewStoryResponse {
        return AddNewStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyErrorUpload(): AddNewStoryResponse {
        return AddNewStoryResponse(
            error = true,
            message = "error"
        )
    }
}