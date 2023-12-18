package com.dicoding.syamsustoryapp.ui.main.maps

import androidx.lifecycle.ViewModel
import com.dicoding.syamsustoryapp.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    //Mendelegasikan panggilan ke StoryRepository yang menangani pengambilan data
    fun getStoryMap() = storyRepository.getStoryWithMap()
}
