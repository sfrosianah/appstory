package com.dicoding.syamsustoryapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.syamsustoryapp.data.StoryRepository
import com.dicoding.syamsustoryapp.data.model.ListStoryItem

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {
    val getAllStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getListStory().cachedIn(viewModelScope)

    fun refreshStories() {
        storyRepository.refreshListStory()
    }
}
