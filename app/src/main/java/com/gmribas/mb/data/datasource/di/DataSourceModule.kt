package com.gmribas.mb.data.datasource.di

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.datasource.exchange.ExchangeDataSource
import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.ExchangeAssetsDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {
    
    @Provides
    fun provideIExchangeDataSource(coinMarketCapApi: CoinMarketCapApi): IExchangeDataSource = 
        ExchangeDataSource(coinMarketCapApi)
    
    @Provides
    fun provideIExchangeAssetsDataSource(coinMarketCapApi: CoinMarketCapApi): IExchangeAssetsDataSource = 
        ExchangeAssetsDataSource(coinMarketCapApi)
}
