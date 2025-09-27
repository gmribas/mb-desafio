package com.gmribas.mb.data.datasource.cryptocurrency

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.Cryptocurrency
import com.gmribas.mb.data.model.CryptocurrencyListingResponse
import com.gmribas.mb.data.model.Quote
import com.gmribas.mb.data.model.Status
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException

class CryptocurrencyDataSourceTest {

    private lateinit var api: CoinMarketCapApi
    private lateinit var dataSource: CryptocurrencyDataSource

    @Before
    fun setUp() {
        api = mockk()
        dataSource = CryptocurrencyDataSource(api)
    }

    @Test
    fun `getLatestListings should return cryptocurrency listing response successfully`() = runTest {
        // Given
        val expectedResponse = createCryptocurrencyListingResponse()
        coEvery { 
            api.getLatestListings(
                start = 1,
                limit = 20,
                convert = "USD",
                sort = "market_cap",
                sortDir = "desc"
            ) 
        } returns expectedResponse

        // When
        val result = dataSource.getLatestListings(
            start = 1,
            limit = 20
        )

        // Then
        assertEquals(expectedResponse, result)
        coVerify { 
            api.getLatestListings(
                start = 1,
                limit = 20,
                convert = "USD",
                sort = "market_cap",
                sortDir = "desc"
            ) 
        }
    }

    @Test
    fun `getLatestListings should handle custom parameters correctly`() = runTest {
        // Given
        val expectedResponse = createCryptocurrencyListingResponse()
        coEvery { 
            api.getLatestListings(any(), any(), any(), any(), any()) 
        } returns expectedResponse

        // When
        val result = dataSource.getLatestListings(
            start = 50,
            limit = 100,
            convert = "EUR",
            sort = "volume_24h",
            sortDir = "asc"
        )

        // Then
        assertEquals(expectedResponse, result)
        coVerify { 
            api.getLatestListings(
                start = 50,
                limit = 100,
                convert = "EUR",
                sort = "volume_24h",
                sortDir = "asc"
            ) 
        }
    }

    @Test(expected = SocketTimeoutException::class)
    fun `getLatestListings should propagate exception when API call fails`() = runTest {
        // Given
        coEvery { 
            api.getLatestListings(any(), any(), any(), any(), any()) 
        } throws SocketTimeoutException("Connection timeout")

        // When
        dataSource.getLatestListings(start = 1, limit = 20)
        
        // Then - Exception is thrown
    }

    private fun createCryptocurrencyListingResponse() = CryptocurrencyListingResponse(
        status = Status(
            timestamp = "2024-01-01T00:00:00.000Z",
            errorCode = 0,
            errorMessage = null,
            elapsed = 10,
            creditCount = 1,
            notice = null,
            totalCount = 1000
        ),
        data = listOf(
            createCryptocurrency("Bitcoin", "BTC", 1, 50000.0),
            createCryptocurrency("Ethereum", "ETH", 2, 3000.0)
        )
    )

    private fun createCryptocurrency(
        name: String,
        symbol: String,
        rank: Int,
        price: Double
    ) = Cryptocurrency(
        id = rank,
        name = name,
        symbol = symbol,
        slug = name.lowercase(),
        numMarketPairs = 100,
        dateAdded = "2024-01-01T00:00:00.000Z",
        tags = listOf("cryptocurrency"),
        maxSupply = 21000000.0,
        circulatingSupply = 19000000.0,
        totalSupply = 19000000.0,
        platform = null,
        cmcRank = rank,
        selfReportedCirculatingSupply = null,
        selfReportedMarketCap = null,
        tvlRatio = null,
        lastUpdated = "2024-01-01T00:00:00.000Z",
        quote = mapOf(
            "USD" to Quote(
                price = price,
                volume24h = 1000000.0,
                volumeChange24h = 5.0,
                percentChange1h = 0.5,
                percentChange24h = 2.0,
                percentChange7d = 10.0,
                percentChange30d = 20.0,
                percentChange60d = 30.0,
                percentChange90d = 40.0,
                marketCap = price * 19000000,
                marketCapDominance = 40.0,
                fullyDilutedMarketCap = price * 21000000,
                tvl = null,
                lastUpdated = "2024-01-01T00:00:00.000Z"
            )
        )
    )
}