package com.dicoding.syamsustoryapp.ui.main

import androidx.lifecycle.ViewModel
import com.dicoding.syamsustoryapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postRegister(
        name: String,
        email: String,
        password: String
    ) = storyRepository.postRegister(name, email, password)
}
