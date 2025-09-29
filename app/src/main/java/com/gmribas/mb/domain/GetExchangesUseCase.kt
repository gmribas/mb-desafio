package com.gmribas.mb.domain

import androidx.paging.PagingData
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.exchange.IExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangesUseCase @Inject constructor(
    private val repository: IExchangeRepository
) {
    operator fun invoke(): Flow<PagingData<ExchangeDTO>> {
        return repository.getExchangePagingData()
    }
    
    suspend fun getLatestListings(
        start: Int = 1,
        limit: Int = 20
    ): UseCaseResult<List<ExchangeDTO>> {
        return try {
            val result = repository.getLatestListings(
                start = start,
                limit = limit
            )
            UseCaseResult.Success(result.exchanges ?: emptyList())
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}