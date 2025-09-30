package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.ExchangeResponse
import com.gmribas.mb.data.model.ExchangeResponseData
import com.gmribas.mb.data.model.Urls
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExchangeDataSourceTest {

    private lateinit var api: CoinMarketCapApi
    private lateinit var dataSource: ExchangeDataSource

    @Before
    fun setUp() {
        api = mockk()
        dataSource = ExchangeDataSource(api)
    }

    @Test
    fun `getExchangeInfo should call api and return response`() = runTest {
        // Given
        val exchangeId = 1
        val expectedData = ExchangeResponseData(
            id = 1,
            name = "Binance",
            slug = "binance",
            description = "Leading cryptocurrency exchange",
            dateLaunched = "2017-07-01T00:00:00.000Z",
            urls = Urls(
                website = listOf("https://www.binance.com"),
                twitter = listOf("https://twitter.com/binance"),
                chat = null,
                fee = null
            ),
            makerFee = 0.0,
            takerFee = 0.0,
            spotVolumeUsd = 0.0,
        )
        val expectedResponse = ExchangeResponse(
            data = mapOf("1" to expectedData)
        )
        
        coEvery { api.getExchangeInfo(exchangeId) } returns expectedResponse

        // When
        val result = dataSource.getExchangeInfo(exchangeId)

        // Then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { api.getExchangeInfo(exchangeId) }
    }

    @Test(expected = Exception::class)
    fun `getExchangeInfo should propagate exception when api fails`() = runTest {
        // Given
        val exchangeId = 1
        coEvery { api.getExchangeInfo(exchangeId) } throws Exception("API error")

        // When
        dataSource.getExchangeInfo(exchangeId)
        
        // Then - exception is expected
    }
}