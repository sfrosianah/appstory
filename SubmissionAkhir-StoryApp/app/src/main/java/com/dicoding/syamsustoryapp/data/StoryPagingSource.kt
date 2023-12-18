package com.dicoding.syamsustoryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.syamsustoryapp.data.model.ListStoryItem
import com.dicoding.syamsustoryapp.retrofit.ApiService
import com.dicoding.syamsustoryapp.data.session.LoginPreferences

class StoryPagingSource(
    private val loginPreferences: LoginPreferences,
    private val apiService: ApiService
) : PagingSource<Int, ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val token = loginPreferences.loginData.token.orEmpty()

            if (token.isNotEmpty()) {
                val response = apiService.getStories("Bearer $token", page, params.loadSize, 0)
                val data = response.body()?.listStory.orEmpty()
                val prevKey = if (page == INITIAL_PAGE) null else page - 1
                val nextKey = if (data.isEmpty()) null else page + 1
                LoadResult.Page(data, prevKey, nextKey)
            } else {
                LoadResult.Error(IllegalStateException("Token Tidak Ada"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}
