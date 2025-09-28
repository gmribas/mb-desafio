package com.gmribas.mb.repository.exchange

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gmribas.mb.data.datasource.cryptocurrency.CryptocurrencyDataSource
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.paging.ExchangePagingSource
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.dto.ExchangeListingDTO
import com.gmribas.mb.repository.mapper.ExchangeListingMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val dataSource: IExchangeDataSource,
    private val listingMapper: ExchangeListingMapper
) : IExchangeRepository {

    override suspend fun getLatestListings(
        start: Int,
        limit: Int,
        sort: String,
    ): ExchangeListingDTO {
        val response = dataSource.getExchangeListings(
            start = start,
            limit = limit,
            sort = sort,
        )
        return listingMapper.toDTO(response)
    }

    override fun getExchangePagingData(): Flow<PagingData<ExchangeDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = CryptocurrencyDataSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = CryptocurrencyDataSource.PAGE_SIZE
            ),
            pagingSourceFactory = {
                ExchangePagingSource(
                    repository = this
                )
            }
        ).flow
    }
}