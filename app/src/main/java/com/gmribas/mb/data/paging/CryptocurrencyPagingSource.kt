package com.gmribas.mb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gmribas.mb.data.datasource.cryptocurrency.CryptocurrencyDataSource
import com.gmribas.mb.repository.cryptocurrency.ICryptocurrencyRepository
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import kotlinx.coroutines.delay

class CryptocurrencyPagingSource(
    private val repository: ICryptocurrencyRepository
) : PagingSource<Int, CryptocurrencyDTO>() {

    override fun getRefreshKey(state: PagingState<Int, CryptocurrencyDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(CryptocurrencyDataSource.PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(CryptocurrencyDataSource.PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptocurrencyDTO> {
        return try {
            val start = params.key ?: 1
            val limit = params.loadSize
            
            // Add a small delay to avoid rate limiting
            if (start > 1) {
                delay(100)
            }
            
            val response = repository.getLatestListings(
                start = start,
                limit = limit
            )

            val cryptocurrencies = response.cryptocurrencies
            val nextKey = if (cryptocurrencies.isEmpty() || cryptocurrencies.size < limit) {
                null
            } else {
                start + limit
            }
            
            val prevKey = if (start == 1) null else (start - limit).coerceAtLeast(1)

            LoadResult.Page(
                data = cryptocurrencies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}