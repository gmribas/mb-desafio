package com.gmribas.mb.ui.matchdetails

import android.util.Log
import app.cash.turbine.test
import com.gmribas.mb.domain.GetMatchOpponentsUseCase
import com.gmribas.mb.domain.UseCaseResult
import com.gmribas.mb.repository.dto.MatchOpponentsResponseDTO
import com.gmribas.mb.repository.dto.MatchResponseDTO
import com.gmribas.mb.repository.dto.TeamDetailsDTO
import com.gmribas.mb.ui.matchdetails.model.MatchDetailsScreenEvent
import com.gmribas.mb.ui.matchdetails.model.MatchDetailsScreenState
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MatchDetailsScreenViewModelTest {

    private lateinit var viewModel: MatchDetailsScreenViewModel
    private lateinit var getMatchOpponentsUseCase: GetMatchOpponentsUseCase
    private lateinit var gson: Gson
    private lateinit var dispatcher: TestDispatcher

    @Before
    fun setUp() {
        getMatchOpponentsUseCase = mockk()
        gson = mockk()
        dispatcher = StandardTestDispatcher()
        viewModel = MatchDetailsScreenViewModel(getMatchOpponentsUseCase, gson)
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `onEvent LoadMatchOpponents should update state to success`() = runTest {
        // Given
        val matchDataJson = "{\"slug\":\"match1\",\"league\":\"league1\",\"serie\":\"serie1\",\"teamA\":null,\"teamB\":null,\"status\":\"running\",\"beginAt\":\"2024-01-01T10:00:00Z\",\"leagueImageUrl\":null,\"isLive\":true,\"formattedDateLabel\":null}"
        val slug = "test-match"
        val matchResponse = MatchResponseDTO("match1", "league1", "serie1", null, null, "running", "2024-01-01T10:00:00Z", null, true, null)
        val opponentsResponse = MatchOpponentsResponseDTO(
            listOf(
                TeamDetailsDTO(
                    id = 1L,
                    name = "Team A",
                    slug = "team-a",
                    acronym = "TA",
                    imageUrl = "https://example.com/team-a.png",
                    players = emptyList()
                )
            )
        )

        every { gson.fromJson(matchDataJson, MatchResponseDTO::class.java) } returns matchResponse
        coEvery { getMatchOpponentsUseCase(slug) } returns UseCaseResult.Success(opponentsResponse)

        // When
        viewModel.state.test {
            val idleState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenIdleState, idleState)
            
            viewModel.onEvent(MatchDetailsScreenEvent.LoadMatchOpponents(matchDataJson, slug))

            // Then
            val loadingState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenLoadingState, loadingState)

            val successState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenSuccessState(matchResponse, opponentsResponse), successState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent LoadMatchOpponents should update state to error on failure`() = runTest {
        // Given
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        val matchDataJson = "{\"slug\":\"match1\",\"league\":\"league1\",\"serie\":\"serie1\",\"teamA\":null,\"teamB\":null,\"status\":\"running\",\"beginAt\":\"2024-01-01T10:00:00Z\",\"leagueImageUrl\":null,\"isLive\":true,\"formattedDateLabel\":null}"
        val slug = "test-match"
        val errorMessage = "Network error"
        val exception = Exception(errorMessage)
        val matchResponse = MatchResponseDTO("match1", "league1", "serie1", null, null, "running", "2024-01-01T10:00:00Z", null, true, null)

        every { gson.fromJson(matchDataJson, MatchResponseDTO::class.java) } returns matchResponse
        coEvery { getMatchOpponentsUseCase(slug) } returns UseCaseResult.Error(exception)

        // When
        viewModel.state.test {
            val idleState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenIdleState, idleState)
            
            viewModel.onEvent(MatchDetailsScreenEvent.LoadMatchOpponents(matchDataJson, slug))

            // Then
            val loadingState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenLoadingState, loadingState)

            val errorState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenErrorState(errorMessage), errorState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent LoadMatchOpponents should handle JSON parsing exception`() = runTest {
        // Given
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        val invalidMatchDataJson = "invalid json"
        val slug = "test-match"

        // When
        viewModel.state.test {
            val idleState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenIdleState, idleState)
            
            viewModel.onEvent(MatchDetailsScreenEvent.LoadMatchOpponents(invalidMatchDataJson, slug))

            // Then
            val loadingState = awaitItem()
            assertEquals(MatchDetailsScreenState.MatchDetailsScreenLoadingState, loadingState)

            val errorState = awaitItem()
            assert(errorState is MatchDetailsScreenState.MatchDetailsScreenErrorState)
            assert((errorState as MatchDetailsScreenState.MatchDetailsScreenErrorState).error != null)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
