package com.gmribas.mb.repository.di

import com.gmribas.mb.data.datasource.cryptocurrency.ICryptocurrencyDataSource
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.repository.cryptocurrency.CryptocurrencyRepository
import com.gmribas.mb.repository.cryptocurrency.ICryptocurrencyRepository
import com.gmribas.mb.repository.exchange.ExchangeRepository
import com.gmribas.mb.repository.exchange.IExchangeRepository
import com.gmribas.mb.repository.mapper.CryptocurrencyListingMapper
import com.gmribas.mb.repository.mapper.CryptocurrencyMapper
import com.gmribas.mb.repository.mapper.ExchangeDetailMapper
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object RepositoryModule {
    
    @Provides
    fun provideCryptocurrencyRepository(
        dataSource: ICryptocurrencyDataSource,
        listingMapper: CryptocurrencyListingMapper
    ): ICryptocurrencyRepository = CryptocurrencyRepository(
        dataSource,
        listingMapper
    )
    
    @Provides
    fun provideExchangeRepository(
        dataSource: IExchangeDataSource,
        assetsDataSource: IExchangeAssetsDataSource,
        mapper: ExchangeDetailMapper,
        assetMapper: ExchangeAssetMapper
    ): IExchangeRepository = ExchangeRepository(
        dataSource = dataSource,
        assetsDataSource = assetsDataSource,
        mapper = mapper, 
        assetMapper = assetMapper
    )
}
