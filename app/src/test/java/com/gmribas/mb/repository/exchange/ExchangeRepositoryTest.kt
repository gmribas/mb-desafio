package com.gmribas.mb.repository.exchange

import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.model.CriptoDetailResponse
import com.gmribas.mb.data.model.CriptoInfoData
import com.gmribas.mb.data.model.CriptoUrls
import com.gmribas.mb.repository.cryptocurrency.CriptoRepository
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExchangeRepositoryTest {

    private lateinit var dataSource: IExchangeDataSource
    private lateinit var mapper: CriptoDetailMapper
    private lateinit var repository: CriptoRepository

    @Before
    fun setUp() {
        dataSource = mockk()
        mapper = mockk()
        repository = CriptoRepository(dataSource, mapper)
    }

    @Test
    fun `getExchangeDetails should fetch data and map to DTO`() = runTest {
        // Given
        val exchangeId = 1
        val criptoInfoData = CriptoInfoData(
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
        
        val response = CriptoDetailResponse(
            status = null,
            data = mapOf("1" to criptoInfoData)
        )
        
        val expectedDTO = ExchangeDetailDTO(
            id = 1,
            name = "Binance",
            symbol = "BNB",
            slug = "binance",
            logo = "https://example.com/logo.png",
            description = "Leading cryptocurrency exchange",
            dateAdded = "2017-07-01T00:00:00.000Z",
            dateLaunched = "2017-07-01T00:00:00.000Z",
            websiteUrl = "https://binance.com",
            category = "Exchange",
            platform = null
        )
        
        coEvery { dataSource.getExchangeInfo(exchangeId) } returns response
        every { mapper.toDTO(criptoInfoData) } returns expectedDTO

        // When
        val result = repository.getCriptoDetails(exchangeId)

        // Then
        assertEquals(expectedDTO, result)
        coVerify(exactly = 1) { dataSource.getExchangeInfo(exchangeId) }
        verify(exactly = 1) { mapper.toDTO(criptoInfoData) }
    }

    @Test(expected = Exception::class)
    fun `getExchangeDetails should throw exception when no data found`() = runTest {
        // Given
        val exchangeId = 1
        val response = CriptoDetailResponse(
            status = null,
            data = emptyMap()
        )
        
        coEvery { dataSource.getExchangeInfo(exchangeId) } returns response

        // When
        repository.getCriptoDetails(exchangeId)
        
        // Then - exception is expected
    }

    @Test(expected = Exception::class)
    fun `getExchangeDetails should throw exception when data is null`() = runTest {
        // Given
        val exchangeId = 1
        val response = CriptoDetailResponse(
            status = null,
            data = null
        )
        
        coEvery { dataSource.getExchangeInfo(exchangeId) } returns response

        // When
        repository.getCriptoDetails(exchangeId)
        
        // Then - exception is expected
    }
}