package com.gmribas.mb.repository.cryptocurrency

import androidx.paging.PagingData
import com.gmribas.mb.data.datasource.cryptocurrency.ICryptocurrencyDataSource
import com.gmribas.mb.data.model.Cryptocurrency
import com.gmribas.mb.data.model.CryptocurrencyListingResponse
import com.gmribas.mb.data.model.Quote
import com.gmribas.mb.data.model.Status
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.repository.mapper.CryptocurrencyListingMapper
import com.gmribas.mb.repository.mapper.CryptocurrencyMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CryptocurrencyRepositoryTest {

    private lateinit var dataSource: ICryptocurrencyDataSource
    private lateinit var listingMapper: CryptocurrencyListingMapper
    private lateinit var repository: CryptocurrencyRepository

    @Before
    fun setUp() {
        dataSource = mockk()
        val cryptocurrencyMapper = CryptocurrencyMapper()
        listingMapper = CryptocurrencyListingMapper(cryptocurrencyMapper)
        repository = CryptocurrencyRepository(dataSource, listingMapper)
    }

    @Test
    fun `getLatestListings should return mapped cryptocurrency listing DTO`() = runTest {
        // Given
        val apiResponse = createCryptocurrencyListingResponse()
        coEvery { 
            dataSource.getLatestListings(any(), any(), any(), any(), any()) 
        } returns apiResponse

        // When
        val result = repository.getLatestListings(
            start = 1,
            limit = 20
        )

        // Then
        assertEquals(2, result.cryptocurrencies.size)
        assertEquals(1000, result.totalCount)
        
        val bitcoin = result.cryptocurrencies[0]
        assertEquals(1, bitcoin.id)
        assertEquals("Bitcoin", bitcoin.name)
        assertEquals("BTC", bitcoin.symbol)
        assertEquals(50000.0, bitcoin.price, 0.01)
        assertEquals(1, bitcoin.rank)
        
        val ethereum = result.cryptocurrencies[1]
        assertEquals(2, ethereum.id)
        assertEquals("Ethereum", ethereum.name)
        assertEquals("ETH", ethereum.symbol)
        assertEquals(3000.0, ethereum.price, 0.01)
        assertEquals(2, ethereum.rank)

        coVerify { 
            dataSource.getLatestListings(
                start = 1,
                limit = 20,
                convert = "USD",
                sort = "market_cap",
                sortDir = "desc"
            ) 
        }
    }

    @Test
    fun `getLatestListings should handle empty response correctly`() = runTest {
        // Given
        val apiResponse = CryptocurrencyListingResponse(
            status = Status(
                timestamp = "2024-01-01T00:00:00.000Z",
                errorCode = 0,
                errorMessage = null,
                elapsed = 10,
                creditCount = 1,
                notice = null,
                totalCount = 0
            ),
            data = emptyList()
        )
        coEvery { 
            dataSource.getLatestListings(any(), any(), any(), any(), any()) 
        } returns apiResponse

        // When
        val result = repository.getLatestListings(
            start = 1,
            limit = 20
        )

        // Then
        assertEquals(0, result.cryptocurrencies.size)
        assertEquals(0, result.totalCount)
    }

    @Test(expected = RuntimeException::class)
    fun `getLatestListings should propagate exception from data source`() = runTest {
        // Given
        coEvery { 
            dataSource.getLatestListings(any(), any(), any(), any(), any()) 
        } throws RuntimeException("API Error")

        // When
        repository.getLatestListings(
            start = 1,
            limit = 20
        )
        
        // Then - Exception is thrown
    }

    @Test
    fun `getCryptocurrenciesPagingData should return paging data flow`() = runTest {
        // When
        val flow = repository.getCryptocurrenciesPagingData()

        // Then
        assertNotNull(flow)
        // Note: Full paging testing requires more complex setup with PagingData testing utilities
    }

    @Test
    fun `mapper should handle null values correctly`() = runTest {
        // Given
        val apiResponse = CryptocurrencyListingResponse(
            status = Status(
                timestamp = "2024-01-01T00:00:00.000Z",
                errorCode = 0,
                errorMessage = null,
                elapsed = 10,
                creditCount = 1,
                notice = null,
                totalCount = 1
            ),
            data = listOf(
                Cryptocurrency(
                    id = 1,
                    name = "Test Coin",
                    symbol = "TEST",
                    slug = "test-coin",
                    numMarketPairs = 0,
                    dateAdded = "2024-01-01T00:00:00.000Z",
                    tags = emptyList(),
                    maxSupply = null,
                    circulatingSupply = null,
                    totalSupply = null,
                    platform = null,
                    cmcRank = 1,
                    selfReportedCirculatingSupply = null,
                    selfReportedMarketCap = null,
                    tvlRatio = null,
                    lastUpdated = "2024-01-01T00:00:00.000Z",
                    quote = emptyMap() // No quote data
                )
            )
        )
        
        coEvery { 
            dataSource.getLatestListings(any(), any(), any(), any(), any()) 
        } returns apiResponse

        // When
        val result = repository.getLatestListings(start = 1, limit = 20)

        // Then
        assertEquals(1, result.cryptocurrencies.size)
        val coin = result.cryptocurrencies[0]
        assertEquals(0.0, coin.price, 0.01) // Default value when quote is missing
        assertEquals(0.0, coin.marketCap, 0.01)
        assertEquals(null, coin.maxSupply)
        assertEquals(null, coin.circulatingSupply)
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