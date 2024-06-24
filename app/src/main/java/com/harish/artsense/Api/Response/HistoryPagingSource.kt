package com.harish.artsense.Api.Response

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.harish.artsense.Api.ApiService

class HistoryPagingSource(private val apiService: ApiService) : PagingSource<Int, HistoryResponseItem>(){
    override fun getRefreshKey(state: PagingState<Int, HistoryResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getHistory(position, params.loadSize)
            val responseData = response.historyResponse?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}