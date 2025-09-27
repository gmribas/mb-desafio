package com.gmribas.mb.repository.cryptocurrency

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gmribas.mb.data.datasource.cryptocurrency.CryptocurrencyDataSource
import com.gmribas.mb.data.datasource.cryptocurrency.ICryptocurrencyDataSource
import com.gmribas.mb.data.paging.CryptocurrencyPagingSource
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.repository.dto.CryptocurrencyListingDTO
import com.gmribas.mb.repository.mapper.CryptocurrencyListingMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CryptocurrencyRepository @Inject constructor(
    private val dataSource: ICryptocurrencyDataSource,
    private val listingMapper: CryptocurrencyListingMapper
) : ICryptocurrencyRepository {

    override suspend fun getLatestListings(
        start: Int,
        limit: Int,
        convert: String,
        sort: String,
        sortDir: String
    ): CryptocurrencyListingDTO {
        val response = dataSource.getLatestListings(
            start = start,
            limit = limit,
            convert = convert,
            sort = sort,
            sortDir = sortDir
        )
        return listingMapper.toDTO(response)
    }

    override fun getCryptocurrenciesPagingData(): Flow<PagingData<CryptocurrencyDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = CryptocurrencyDataSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = CryptocurrencyDataSource.PAGE_SIZE
            ),
            pagingSourceFactory = {
                CryptocurrencyPagingSource(
                    repository = this
                )
            }
        ).flow
    }
}