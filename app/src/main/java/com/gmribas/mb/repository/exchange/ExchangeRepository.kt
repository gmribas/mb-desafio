package com.gmribas.mb.repository.exchange

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.data.paging.ExchangePagingSource
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.dto.ExchangeListingDTO
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import com.gmribas.mb.repository.mapper.ExchangeListingMapper
import com.gmribas.mb.repository.mapper.ExchangeMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val dataSource: IExchangeDataSource,
    private val assetsDataSource: IExchangeAssetsDataSource,
    private val listingMapper: ExchangeListingMapper,
    private val mapper: ExchangeMapper,
    private val assetMapper: ExchangeAssetMapper
) : IExchangeRepository {

    override suspend fun getInfo(id: Int): ExchangeDTO {
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
                pageSize = ExchangePagingSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = ExchangePagingSource.PAGE_SIZE
            ),
            pagingSourceFactory = {
                ExchangePagingSource(
                    repository = this
                )
            }
        ).flow
    }

    override suspend fun getExchangeAssets(id: Int): List<ExchangeAssetDTO> {
        val response = assetsDataSource.getExchangeAssets(id)
        val assets = response.data ?: emptyList()
        return assetMapper.toDTOList(assets)
    }
}