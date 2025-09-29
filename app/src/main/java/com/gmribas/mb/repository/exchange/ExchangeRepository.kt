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
import com.gmribas.mb.repository.mapper.ExchangeMapMapper
import com.gmribas.mb.repository.mapper.ExchangeMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val dataSource: IExchangeDataSource,
    private val listingMapper: ExchangeListingMapper,
    private val mapper: ExchangeMapper,
) : IExchangeRepository {

    override suspend fun getInfo(id: Int): ExchangeDTO {
        val hhue = dataSource.getExchangeInfo(id)
        println("HUE $hhue")
        println("HUE map ${mapper.toDTO(hhue)}")
        return mapper.toDTO(dataSource.getExchangeInfo(id))
    }

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