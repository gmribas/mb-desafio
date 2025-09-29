package com.gmribas.mb.repository.di

import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.repository.exchange.ExchangeRepository
import com.gmribas.mb.repository.exchange.IExchangeRepository
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import com.gmribas.mb.repository.mapper.ExchangeListingMapper
import com.gmribas.mb.repository.mapper.ExchangeMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object RepositoryModule {

    @Provides
    fun provideExchangeRepository(
        dataSource: IExchangeDataSource,
        assetsDataSource: IExchangeAssetsDataSource,
        listingMapper: ExchangeListingMapper,
        mapper: ExchangeMapper,
        assetMapper: ExchangeAssetMapper
    ): IExchangeRepository = ExchangeRepository(
        dataSource = dataSource,
        assetsDataSource = assetsDataSource,
        listingMapper = listingMapper,
        mapper = mapper,
        assetMapper = assetMapper
    )
}
