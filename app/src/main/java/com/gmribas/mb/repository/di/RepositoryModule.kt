package com.gmribas.mb.repository.di

import com.gmribas.mb.data.datasource.cryptocurrency.ICryptocurrencyDataSource
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.repository.cryptocurrency.CryptocurrencyRepository
import com.gmribas.mb.repository.cryptocurrency.ICryptocurrencyRepository
import com.gmribas.mb.repository.cryptocurrency.CriptoRepository
import com.gmribas.mb.repository.cryptocurrency.ICriptoRepository
import com.gmribas.mb.repository.exchange.ExchangeRepository
import com.gmribas.mb.repository.exchange.IExchangeRepository
import com.gmribas.mb.repository.mapper.CryptocurrencyListingMapper
import com.gmribas.mb.repository.mapper.CriptoDetailMapper
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import com.gmribas.mb.repository.mapper.ExchangeListingMapper
import com.gmribas.mb.repository.mapper.ExchangeMapMapper
import com.gmribas.mb.repository.mapper.ExchangeMapper
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
    fun provideCriptoRepository(
        dataSource: ICryptocurrencyDataSource,
        assetsDataSource: IExchangeAssetsDataSource,
        mapper: CriptoDetailMapper,
        assetMapper: ExchangeAssetMapper
    ): ICriptoRepository = CriptoRepository(
        dataSource = dataSource,
        assetsDataSource = assetsDataSource,
        mapper = mapper, 
        assetMapper = assetMapper
    )

    @Provides
    fun provideExchangeRepository(
        dataSource: IExchangeDataSource,
        listingMapper: ExchangeListingMapper,
        mapper: ExchangeMapper
    ): IExchangeRepository = ExchangeRepository(
        dataSource = dataSource,
        listingMapper = listingMapper,
        mapper = mapper
    )
}
