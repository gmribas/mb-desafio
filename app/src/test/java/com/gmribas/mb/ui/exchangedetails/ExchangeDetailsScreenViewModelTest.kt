package com.gmribas.mb.ui.exchangedetails

import androidx.lifecycle.SavedStateHandle
import com.gmribas.mb.domain.UseCaseResult
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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

    private lateinit var getCriptoDetailsUseCase: GetCriptoDetailsUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ExchangeDetailsScreenViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val exchangeId = 1

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getExchangeDetailsUseCase = mockk()
        savedStateHandle = mockk()
        every { savedStateHandle.get<Int>("id") } returns exchangeId
    }

    private fun createViewModel(): ExchangeDetailsScreenViewModel {
        return ExchangeDetailsScreenViewModel(savedStateHandle, getExchangeDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load exchange details on creation`() = runTest {
        // Given
        val exchangeDTO = createExchangeDTO()
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Success(exchangeDTO)
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        
        // Then
        assertEquals(ExchangeDetailsScreenState.Success(exchangeDTO), viewModel.state.value)
        coVerify(exactly = 1) { getExchangeDetailsUseCase(exchangeId) }
    }

    @Test
    fun `init should handle error when loading fails`() = runTest {
        // Given
        val errorMessage = "Failed to load exchange"
        val exception = Exception(errorMessage)
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Error(exception)
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.state.value is ExchangeDetailsScreenState.Error)
        assertEquals(errorMessage, (viewModel.state.value as ExchangeDetailsScreenState.Error).message)
        coVerify(exactly = 1) { getExchangeDetailsUseCase(exchangeId) }
    }

    @Test
    fun `init should handle invalid exchange ID`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("id") } returns 0
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.state.value is ExchangeDetailsScreenState.Error)
        assertEquals("Invalid exchange ID", (viewModel.state.value as ExchangeDetailsScreenState.Error).message)
        coVerify(exactly = 0) { getExchangeDetailsUseCase(any()) }
    }

    @Test
    fun `RetryLoad event should reload exchange details`() = runTest {
        // Given
        val exchangeDTO = createExchangeDTO()
        coEvery { getExchangeDetailsUseCase(exchangeId) } returnsMany listOf(
            UseCaseResult.Error(Exception("First attempt failed")),
            UseCaseResult.Success(exchangeDTO)
        )
        
        viewModel = createViewModel()
        advanceUntilIdle()
        
        // When - trigger retry
        viewModel.onEvent(ExchangeDetailsScreenEvent.RetryLoad)
        advanceUntilIdle()
        
        // Then
        assertEquals(ExchangeDetailsScreenState.Success(exchangeDTO), viewModel.state.value)
        coVerify(exactly = 2) { getExchangeDetailsUseCase(exchangeId) }
    }

    @Test
    fun `RetryLoad event should not reload if invalid ID`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("id") } returns 0
        viewModel = createViewModel()
        advanceUntilIdle()
        
        // When
        viewModel.onEvent(ExchangeDetailsScreenEvent.RetryLoad)
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.state.value is ExchangeDetailsScreenState.Error)
        coVerify(exactly = 0) { getExchangeDetailsUseCase(any()) }
    }

    @Test
    fun `OpenWebsite event should not change state`() = runTest {
        // Given
        val url = "https://binance.com"
        val exchangeDTO = createExchangeDTO()
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Success(exchangeDTO)
        viewModel = createViewModel()
        advanceUntilIdle()
        
        val initialState = viewModel.state.value
        
        // When
        viewModel.onEvent(ExchangeDetailsScreenEvent.OpenWebsite(url))
        advanceUntilIdle()
        
        // Then
        assertEquals(initialState, viewModel.state.value)
    }

    @Test
    fun `init should handle null error message`() = runTest {
        // Given
        val exception = Exception(null as String?)
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Error(exception)
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.state.value is ExchangeDetailsScreenState.Error)
        assertEquals("Unknown error occurred", (viewModel.state.value as ExchangeDetailsScreenState.Error).message)
    }
    
    private fun createExchangeDTO() = ExchangeDetailDTO(
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
}
