package com.dicoding.syamsustoryapp.data.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.syamsustoryapp.data.StoryRepository
import com.dicoding.syamsustoryapp.di.Injection
import com.dicoding.syamsustoryapp.ui.detail.AddStoryViewModel
import com.dicoding.syamsustoryapp.ui.main.LoginViewModel
import com.dicoding.syamsustoryapp.ui.main.MainViewModel
import com.dicoding.syamsustoryapp.ui.main.maps.MapsViewModel
import com.dicoding.syamsustoryapp.ui.main.RegisterViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository): ViewModelProvider.NewInstanceFactory() {

    private val creators: Map<Class<out ViewModel>, () -> ViewModel> = mapOf(
        MainViewModel::class.java to { MainViewModel(storyRepository) },
        LoginViewModel::class.java to { LoginViewModel(storyRepository) },
        RegisterViewModel::class.java to { RegisterViewModel(storyRepository) },
        MapsViewModel::class.java to { MapsViewModel(storyRepository) },
        AddStoryViewModel::class.java to { AddStoryViewModel(storyRepository) }
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        return creator() as T
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}
