package com.gmribas.mb.ui.exchangedetails

import androidx.lifecycle.SavedStateHandle
import com.gmribas.mb.domain.GetExchangeAssetsUseCase
import com.gmribas.mb.domain.GetExchangeDetailsUseCase
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.gmribas.mb.domain.*

@ExperimentalCoroutinesApi
class ExchangeDetailsScreenViewModelTest {

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getExchangeDetailsUseCase: GetExchangeDetailsUseCase = mockk()
    private val getExchangeAssetsUseCase: GetExchangeAssetsUseCase = mockk()

    private lateinit var viewModel: ExchangeDetailsScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ExchangeDetailsScreenViewModel(
            savedStateHandle,
            getExchangeDetailsUseCase,
            getExchangeAssetsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit error when exchangeId is invalid`() = runTest {
        every { savedStateHandle.get<Int>("id") } returns null

        viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails)

        assert(viewModel.state.value is ExchangeDetailsScreenState.Error)
        assert((viewModel.state.value as ExchangeDetailsScreenState.Error).message == "Invalid exchange ID")
    }

    @Test
    fun `should emit success and load assets when exchange details are fetched`() = runTest {
        val exchangeId = 1
        val exchangeDetails = mockk<Any>()
        val assets = listOf(mockk<Any>())

        every { savedStateHandle.get<Int>("id") } returns exchangeId
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Success(exchangeDetails)
        coEvery { getExchangeAssetsUseCase(exchangeId) } returns UseCaseResult.Success(assets)

        viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails)

        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is ExchangeDetailsScreenState.Success)
        val successState = state as ExchangeDetailsScreenState.Success
        assert(successState.exchange == exchangeDetails)
        assert(successState.assets == assets)
        assert(!successState.assetsLoading)
    }

    @Test
    fun `should emit error when exchange details fail`() = runTest {
        val exchangeId = 1
        val error = Throwable("Network error")

        every { savedStateHandle.get<Int>("id") } returns exchangeId
        coEvery { getExchangeDetailsUseCase(exchangeId) } returns UseCaseResult.Error(error)

        viewModel.onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails)

        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is ExchangeDetailsScreenState.Error)
        assert((state as ExchangeDetailsScreenState.Error).message == "Network error")
    }
}

