package com.gmribas.mb.ui.exchangedetails

import app.cash.turbine.test
import com.gmribas.mb.domain.GetExchangeDetailsUseCase
import com.gmribas.mb.domain.UseCaseResult
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeDetailsScreenViewModelTest {

    private lateinit var getExchangeDetailsUseCase: GetExchangeDetailsUseCase
    private lateinit var viewModel: ExchangeDetailsScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getExchangeDetailsUseCase = mockk()
        viewModel = ExchangeDetailsScreenViewModel(getExchangeDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        // Then
        assertEquals(ExchangeDetailsScreenState.Loading, viewModel.state.value)
    }

    @Test
    fun `LoadExchangeDetails event should fetch data successfully`() = runTest {
        // Given
        val exchangeId = 1
        val exchangeDTO = ExchangeDetailDTO(
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
        
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Success(exchangeDTO)

        // When
        viewModel.state.test {
            assertEquals(ExchangeDetailsScreenState.Loading, awaitItem())
            
            viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails(exchangeId))
            advanceUntilIdle()
            
            val successState = awaitItem()
            assertTrue(successState is ExchangeDetailsScreenState.Success)
            assertEquals(exchangeDTO, (successState as ExchangeDetailsScreenState.Success).exchange)
        }

        // Then
        coVerify(exactly = 1) { getExchangeDetailsUseCase(exchangeId) }
    }

    @Test
    fun `LoadExchangeDetails event should handle error`() = runTest {
        // Given
        val exchangeId = 1
        val errorMessage = "Failed to load exchange"
        val exception = Exception(errorMessage)
        
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Error(exception)

        // When
        viewModel.state.test {
            assertEquals(ExchangeDetailsScreenState.Loading, awaitItem())
            
            viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails(exchangeId))
            advanceUntilIdle()
            
            val errorState = awaitItem()
            assertTrue(errorState is ExchangeDetailsScreenState.Error)
            assertEquals(errorMessage, (errorState as ExchangeDetailsScreenState.Error).message)
        }

        // Then
        coVerify(exactly = 1) { getExchangeDetailsUseCase(exchangeId) }
    }

    @Test
    fun `RetryLoad event should reload using stored exchange ID`() = runTest {
        // Given
        val exchangeId = 1
        val exchangeDTO = ExchangeDetailDTO(
            id = 1,
            name = "Binance",
            symbol = "BNB",
            slug = "binance",
            logo = null,
            description = null,
            dateAdded = null,
            dateLaunched = null,
            websiteUrl = null,
            category = null,
            platform = null
        )
        
        coEvery { getExchangeDetailsUseCase(exchangeId) } returnsMany listOf(
            UseCaseResult.Error(Exception("First attempt failed")),
            UseCaseResult.Success(exchangeDTO)
        )

        // When
        viewModel.state.test {
            assertEquals(ExchangeDetailsScreenState.Loading, awaitItem())
            
            // First load that fails
            viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails(exchangeId))
            advanceUntilIdle()
            
            val errorState = awaitItem()
            assertTrue(errorState is ExchangeDetailsScreenState.Error)
            
            // Retry that succeeds
            viewModel.onEvent(ExchangeDetailsScreenEvent.RetryLoad)
            advanceUntilIdle()
            
            assertEquals(ExchangeDetailsScreenState.Loading, awaitItem())
            
            val successState = awaitItem()
            assertTrue(successState is ExchangeDetailsScreenState.Success)
            assertEquals(exchangeDTO, (successState as ExchangeDetailsScreenState.Success).exchange)
        }

        // Then
        coVerify(exactly = 2) { getExchangeDetailsUseCase(exchangeId) }
    }

    @Test
    fun `RetryLoad event should do nothing if no exchange ID stored`() = runTest {
        // When
        viewModel.onEvent(ExchangeDetailsScreenEvent.RetryLoad)
        advanceUntilIdle()

        // Then
        assertEquals(ExchangeDetailsScreenState.Loading, viewModel.state.value)
        coVerify(exactly = 0) { getExchangeDetailsUseCase(any()) }
    }

    @Test
    fun `OpenWebsite event should not change state`() = runTest {
        // Given
        val url = "https://binance.com"

        // When
        viewModel.onEvent(ExchangeDetailsScreenEvent.OpenWebsite(url))
        advanceUntilIdle()

        // Then
        assertEquals(ExchangeDetailsScreenState.Loading, viewModel.state.value)
    }

    @Test
    fun `LoadExchangeDetails should handle null error message`() = runTest {
        // Given
        val exchangeId = 1
        val exception = Exception(null as String?)
        
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Error(exception)

        // When
        viewModel.state.test {
            assertEquals(ExchangeDetailsScreenState.Loading, awaitItem())
            
            viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails(exchangeId))
            advanceUntilIdle()
            
            val errorState = awaitItem()
            assertTrue(errorState is ExchangeDetailsScreenState.Error)
            assertEquals("Unknown error occurred", (errorState as ExchangeDetailsScreenState.Error).message)
        }
    }
}