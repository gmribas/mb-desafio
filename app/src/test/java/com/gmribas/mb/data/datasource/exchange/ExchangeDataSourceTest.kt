package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.ExchangeDetailResponse
import com.gmribas.mb.data.model.ExchangeInfoData
import com.gmribas.mb.data.model.ExchangeUrls
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
        val expectedData = ExchangeInfoData(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "https://example.com/logo.png",
            description = "Leading cryptocurrency exchange",
            dateLaunched = "2017-07-01T00:00:00.000Z",
            urls = ExchangeUrls(
                website = listOf("https://binance.com"),
                twitter = null,
                blog = null,
                chat = null,
                fee = null
            ),
            makerFee = 0.1,
            takerFee = 0.1
        )
        val expectedResponse = ExchangeDetailResponse(
            status = null,
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