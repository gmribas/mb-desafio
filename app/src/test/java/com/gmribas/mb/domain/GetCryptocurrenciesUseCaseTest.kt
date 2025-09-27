package com.gmribas.mb.domain

import androidx.paging.PagingData
import com.gmribas.mb.repository.cryptocurrency.ICryptocurrencyRepository
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.repository.dto.CryptocurrencyListingDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCryptocurrenciesUseCaseTest {

    private lateinit var repository: ICryptocurrencyRepository
    private lateinit var useCase: GetCryptocurrenciesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCryptocurrenciesUseCase(repository)
    }

    @Test
    fun `invoke should return paging data flow from repository`() = runTest {
        // Given
        val pagingData = PagingData.from(listOf(createCryptocurrencyDTO("Bitcoin", "BTC")))
        every { repository.getCryptocurrenciesPagingData() } returns flowOf(pagingData)

        // When
        val result = useCase()

        // Then
        assertNotNull(result)
        val firstEmission = result.first()
        assertNotNull(firstEmission)
        verify { repository.getCryptocurrenciesPagingData() }
    }

    @Test
    fun `getLatestListings should return success with cryptocurrency list`() = runTest {
        // Given
        val cryptocurrencies = listOf(
            createCryptocurrencyDTO("Bitcoin", "BTC"),
            createCryptocurrencyDTO("Ethereum", "ETH")
        )
        val listingDTO = CryptocurrencyListingDTO(
            cryptocurrencies = cryptocurrencies,
            totalCount = 1000
        )
        
        coEvery { 
            repository.getLatestListings(any(), any(), any(), any(), any()) 
        } returns listingDTO

        // When
        val result = useCase.getLatestListings(start = 1, limit = 20)

        // Then
        assertTrue(result is UseCaseResult.Success)
        assertEquals(2, (result as UseCaseResult.Success).data.size)
        assertEquals("Bitcoin", result.data[0].name)
        assertEquals("Ethereum", result.data[1].name)
        
        coVerify { 
            repository.getLatestListings(
                start = 1,
                limit = 20,
                convert = "USD",
                sort = "market_cap",
                sortDir = "desc"
            ) 
        }
    }

    @Test
    fun `getLatestListings should return error when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { 
            repository.getLatestListings(any(), any(), any(), any(), any()) 
        } throws exception

        // When
        val result = useCase.getLatestListings(start = 1, limit = 20)

        // Then
        assertTrue(result is UseCaseResult.Error)
        assertEquals(exception, (result as UseCaseResult.Error).error)
    }

    @Test
    fun `getLatestListings should handle empty list correctly`() = runTest {
        // Given
        val listingDTO = CryptocurrencyListingDTO(
            cryptocurrencies = emptyList(),
            totalCount = 0
        )
        
        coEvery { 
            repository.getLatestListings(any(), any(), any(), any(), any()) 
        } returns listingDTO

        // When
        val result = useCase.getLatestListings(start = 1, limit = 20)

        // Then
        assertTrue(result is UseCaseResult.Success)
        assertEquals(0, (result as UseCaseResult.Success).data.size)
    }

    @Test
    fun `getLatestListings should use custom parameters`() = runTest {
        // Given
        val listingDTO = CryptocurrencyListingDTO(
            cryptocurrencies = listOf(createCryptocurrencyDTO("Bitcoin", "BTC")),
            totalCount = 1
        )
        
        coEvery { 
            repository.getLatestListings(any(), any(), any(), any(), any()) 
        } returns listingDTO

        // When
        val result = useCase.getLatestListings(start = 50, limit = 100)

        // Then
        assertTrue(result is UseCaseResult.Success)
        
        coVerify { 
            repository.getLatestListings(
                start = 50,
                limit = 100,
                convert = "USD",
                sort = "market_cap",
                sortDir = "desc"
            ) 
        }
    }

    @Test
    fun `getLatestListings should use default parameters when not provided`() = runTest {
        // Given
        val listingDTO = CryptocurrencyListingDTO(
            cryptocurrencies = listOf(createCryptocurrencyDTO("Bitcoin", "BTC")),
            totalCount = 1
        )
        
        coEvery { 
            repository.getLatestListings(any(), any(), any(), any(), any()) 
        } returns listingDTO

        // When
        val result = useCase.getLatestListings()

        // Then
        assertTrue(result is UseCaseResult.Success)
        
        coVerify { 
            repository.getLatestListings(
                start = 1,
                limit = 20,
                convert = "USD",
                sort = "market_cap",
                sortDir = "desc"
            ) 
        }
    }

    private fun createCryptocurrencyDTO(name: String, symbol: String) = CryptocurrencyDTO(
        id = 1,
        name = name,
        symbol = symbol,
        slug = name.lowercase(),
        rank = 1,
        price = 50000.0,
        volume24h = 1000000.0,
        marketCap = 1000000000.0,
        percentChange1h = 0.5,
        percentChange24h = 2.0,
        percentChange7d = 10.0,
        circulatingSupply = 19000000.0,
        totalSupply = 19000000.0,
        maxSupply = 21000000.0,
        lastUpdated = "2024-01-01T00:00:00.000Z",
        dateAdded = "2009-01-03T00:00:00.000Z"
    )
}