package com.dicoding.syamsustoryapp.ui.main

import androidx.lifecycle.ViewModel
import com.dicoding.syamsustoryapp.data.StoryRepository


class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun postLogin(
        email: String,
        password: String
    ) = storyRepository.postLogin(email, password)
}
