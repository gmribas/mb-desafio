package com.gmribas.mb.repository.di

import com.gmribas.mb.data.datasource.cryptocurrency.ICryptocurrencyDataSource
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.repository.cryptocurrency.CryptocurrencyRepository
import com.gmribas.mb.repository.cryptocurrency.ICryptocurrencyRepository
import com.gmribas.mb.repository.exchange.ExchangeRepository
import com.gmribas.mb.repository.exchange.IExchangeRepository
import com.gmribas.mb.repository.mapper.CryptocurrencyListingMapper
import com.gmribas.mb.repository.mapper.CryptocurrencyMapper
import com.gmribas.mb.repository.mapper.ExchangeDetailMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    
    @Provides
    fun provideCryptocurrencyMapper(): CryptocurrencyMapper = CryptocurrencyMapper()
    
    @Provides
    fun provideCryptocurrencyListingMapper(
        cryptocurrencyMapper: CryptocurrencyMapper
    ): CryptocurrencyListingMapper = CryptocurrencyListingMapper(cryptocurrencyMapper)
    
    @Provides
    fun provideCryptocurrencyRepository(
        dataSource: ICryptocurrencyDataSource,
        listingMapper: CryptocurrencyListingMapper
    ): ICryptocurrencyRepository = CryptocurrencyRepository(
        dataSource,
        listingMapper
    )
    
    @Provides
    fun provideExchangeDetailMapper(): ExchangeDetailMapper = ExchangeDetailMapper()
    
    @Provides
    fun provideExchangeRepository(
        dataSource: IExchangeDataSource,
        mapper: ExchangeDetailMapper
    ): IExchangeRepository = ExchangeRepository(
        dataSource,
        mapper
    )
}
