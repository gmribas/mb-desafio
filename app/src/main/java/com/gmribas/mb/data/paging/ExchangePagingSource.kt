package com.gmribas.mb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gmribas.mb.data.datasource.cryptocurrency.CryptocurrencyDataSource
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.exchange.IExchangeRepository
import kotlinx.coroutines.delay

class ExchangePagingSource(
    private val repository: IExchangeRepository
) : PagingSource<Int, ExchangeDTO>() {

    override fun getRefreshKey(state: PagingState<Int, ExchangeDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(CryptocurrencyDataSource.PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(CryptocurrencyDataSource.PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExchangeDTO> {
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

            val exchanges = response.exchanges
            val nextKey = if (exchanges.isEmpty() || exchanges.size < limit) {
                null
            } else {
                start + limit
            }
            
            val prevKey = if (start == 1) null else (start - limit).coerceAtLeast(1)

            LoadResult.Page(
                data = exchanges,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}