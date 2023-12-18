package com.dicoding.syamsustoryapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.syamsustoryapp.ui.main.adapter.StoryAdapter
import com.dicoding.syamsustoryapp.data.StoryRepository
import com.dicoding.syamsustoryapp.data.model.ListStoryItem
import com.dicoding.syamsustoryapp.utils.DataDummy
import com.dicoding.syamsustoryapp.utils.MainDispatcherRule
import com.dicoding.syamsustoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private var dataDummyStory = DataDummy.generateDummyDataStory()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get Story Should Not Null and Success`() = runTest {
        val mData: PagingData<ListStoryItem> = PagingSourceStory.snapshot(dataDummyStory.listStory)
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()

        expectedResponse.value = mData
        Mockito.`when`(storyRepository.getListStory()).thenReturn(expectedResponse)

        val homeViewModel = MainViewModel(storyRepository)
        val actual: PagingData<ListStoryItem> = homeViewModel.getAllStory.getOrAwaitValue()

        val diff = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        diff.submitData(actual)

        Assert.assertNotNull(diff.snapshot())
        Assert.assertEquals(dataDummyStory.listStory, diff.snapshot().toList())
        Assert.assertEquals(dataDummyStory.listStory.size, diff.snapshot().size)
        Assert.assertEquals(dataDummyStory.listStory[0], diff.snapshot()[0])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get Story Should Handle No Data Case`() = runTest {
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResponse.value = PagingData.empty()

        Mockito.`when`(storyRepository.getListStory()).thenReturn(expectedResponse)

        val homeViewModel = MainViewModel(storyRepository)
        val actual: PagingData<ListStoryItem> = homeViewModel.getAllStory.getOrAwaitValue()

        val diff = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        diff.submitData(actual)

        Assert.assertTrue(diff.snapshot().isEmpty())
    }

}

class PagingSourceStory : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), null, null)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
