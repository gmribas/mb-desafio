package com.gmribas.mb.data.datasource.exchange

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.CriptoDetailResponse
import com.gmribas.mb.data.model.CriptoInfoData
import com.gmribas.mb.data.model.CriptoUrls
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
        val expectedData = CriptoInfoData(
            id = 1,
            name = "Binance",
            symbol = "BNB",
            slug = "binance",
            logo = "https://example.com/logo.png",
            description = "Leading cryptocurrency exchange",
            dateAdded = "2017-07-01T00:00:00.000Z",
            dateLaunched = "2017-07-01T00:00:00.000Z",
            urls = CriptoUrls(
                website = listOf("https://binance.com"),
                technicalDoc = null,
                twitter = null,
                reddit = null,
                messageBoard = null,
                announcement = null,
                chat = null,
                explorer = null,
                sourceCode = null
            ),
            category = "Exchange",
            platform = null
        )
        val expectedResponse = CriptoDetailResponse(
            status = null,
            data = mapOf("1" to expectedData)
        )
        
        coEvery { api.getCryptocurrencyInfo(exchangeId) } returns expectedResponse

        // When
        val result = dataSource.getExchangeInfo(exchangeId)

        // Then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { api.getCryptocurrencyInfo(exchangeId) }
    }

    @Test(expected = Exception::class)
    fun `getExchangeInfo should propagate exception when api fails`() = runTest {
        // Given
        val exchangeId = 1
        coEvery { api.getCryptocurrencyInfo(exchangeId) } throws Exception("API error")

        // When
        dataSource.getExchangeInfo(exchangeId)
        
        // Then - exception is expected
    }
}