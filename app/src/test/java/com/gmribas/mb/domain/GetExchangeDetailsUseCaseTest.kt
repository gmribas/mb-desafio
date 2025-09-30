package com.gmribas.mb.domain

import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.dto.UrlsDTO
import com.gmribas.mb.repository.exchange.IExchangeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetExchangeDetailsUseCaseTest {

    private lateinit var repository: IExchangeRepository
    private lateinit var useCase: GetExchangeDetailsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetExchangeDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return Success when repository returns data`() = runTest {
        // Given
        val exchangeId = 1
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
        
        coEvery { repository.getInfo(exchangeId) } returns expectedDTO

        // When
        val result = useCase(exchangeId)

        // Then
        assertTrue(result is UseCaseResult.Success)
        assertEquals(expectedDTO, (result as UseCaseResult.Success).data)
        coVerify(exactly = 1) { repository.getInfo(exchangeId) }
    }

    @Test
    fun `invoke should return Error when repository throws exception`() = runTest {
        // Given
        val exchangeId = 1
        val exception = Exception("Repository error")
        
        coEvery { repository.getInfo(exchangeId) } throws exception

        // When
        val result = useCase(exchangeId)

        // Then
        assertTrue(result is UseCaseResult.Error)
        assertEquals(exception, (result as UseCaseResult.Error).error)
        coVerify(exactly = 1) { repository.getInfo(exchangeId) }
    }

    @Test
    fun `invoke should handle specific error messages`() = runTest {
        // Given
        val exchangeId = 999
        val specificError = IllegalArgumentException("Exchange not found")
        
        coEvery { repository.getInfo(exchangeId) } throws specificError

        // When
        val result = useCase(exchangeId)

        // Then
        assertTrue(result is UseCaseResult.Error)
        val error = result as UseCaseResult.Error
        assertEquals("Exchange not found", error.error.message)
        assertTrue(error.error is IllegalArgumentException)
    }
}