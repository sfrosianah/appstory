package com.dicoding.syamsustoryapp.ui.main.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.syamsustoryapp.data.StoryRepository
import com.dicoding.syamsustoryapp.data.model.StoryResponse
import com.dicoding.syamsustoryapp.utils.DataDummy
import com.dicoding.syamsustoryapp.data.Result
import com.dicoding.syamsustoryapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private var dataDummy = DataDummy.generateDummyDataStory()

    @Before
    fun setup(){
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Success(dataDummy)
        Mockito.`when`(storyRepository.getStoryWithMap()).thenReturn(expectedResponse)
        val actual = mapsViewModel.getStoryMap().getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryWithMap()
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Success)
    }

    @Test
    fun `when Get Story Should Not Null and Return Error`() {
        dataDummy = DataDummy.generateDummyErrorDataStory()
        val expected = MutableLiveData<Result<StoryResponse>>()
        expected.value = Result.Error("error")
        Mockito.`when`(storyRepository.getStoryWithMap()).thenReturn(expected)
        val actual = mapsViewModel.getStoryMap().getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryWithMap()
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Error)
    }

}