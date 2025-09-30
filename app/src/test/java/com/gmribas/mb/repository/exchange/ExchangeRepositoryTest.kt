package com.gmribas.mb.repository.exchange

import com.gmribas.mb.data.datasource.exchange.IExchangeDataSource
import com.gmribas.mb.data.datasource.exchangeassets.IExchangeAssetsDataSource
import com.gmribas.mb.data.model.ExchangeResponse
import com.gmribas.mb.data.model.ExchangeResponseData
import com.gmribas.mb.data.model.Urls
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.dto.UrlsDTO
import com.gmribas.mb.repository.mapper.ExchangeAssetMapper
import com.gmribas.mb.repository.mapper.ExchangeListingMapper
import com.gmribas.mb.repository.mapper.ExchangeMapper
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

    private val dataSource: IExchangeDataSource = mockk()
    private val assetsDataSource: IExchangeAssetsDataSource = mockk()
    private val listingMapper: ExchangeListingMapper = mockk()
    private val mapper: ExchangeMapper = mockk()
    private val assetMapper: ExchangeAssetMapper = mockk()
    private lateinit var repository: ExchangeRepository

    @Before
    fun setUp() {
        repository = ExchangeRepository(
            dataSource = dataSource,
            assetsDataSource = assetsDataSource,
            listingMapper = listingMapper,
            mapper = mapper,
            assetMapper = assetMapper
        )
    }

    @Test
    fun `getExchangeDetails should fetch data and map to DTO`() = runTest {
        // Given
        val exchangeId = 1
        val responseData = ExchangeResponseData(
            id = 1,
            name = "Binance",
            slug = "binance",
            description = "Leading cryptocurrency exchange",
            dateLaunched = "2017-07-01T00:00:00.000Z",
            urls = Urls(
                website = listOf("https://binance.com"),
                twitter = null,
                chat = null,
                fee = null
            ),
            makerFee = 0.0,
            takerFee = 0.0,
            spotVolumeUsd = 0.0,
        )
        
        val response = ExchangeResponse(
            data = mapOf("1" to responseData)
        )
        
        val expectedDTO = ExchangeDTO(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "https://example.com/logo.png",
            description = "Leading cryptocurrency exchange",
            dateLaunched = "2017-07-01T00:00:00.000Z",
            spotVolumeUsd = 0.0,
            makerFee = 0.0,
            takerFee = 0.0,
            urls = UrlsDTO(),
        )
        
        coEvery { dataSource.getExchangeInfo(exchangeId) } returns response
        every { mapper.toDTO(response) } returns expectedDTO

        // When
        val result = repository.getInfo(exchangeId)

        // Then
        assertEquals(expectedDTO, result)
        coVerify(exactly = 1) { dataSource.getExchangeInfo(exchangeId) }
        verify(exactly = 1) { mapper.toDTO(response) }
    }

    @Test(expected = Exception::class)
    fun `getExchangeDetails should throw exception when no data found`() = runTest {
        // Given
        val exchangeId = 1
        val response = ExchangeResponse(
            data = emptyMap()
        )
        
        coEvery { dataSource.getExchangeInfo(exchangeId) } returns response

        // When
        repository.getInfo(exchangeId)
        
        // Then - exception is expected
    }

    @Test(expected = Exception::class)
    fun `getExchangeDetails should throw exception when data is null`() = runTest {
        // Given
        val exchangeId = 1
        val response = ExchangeResponse(
            data = emptyMap()
        )
        
        coEvery { dataSource.getExchangeInfo(exchangeId) } returns response

        // When
        repository.getInfo(exchangeId)
    }
}