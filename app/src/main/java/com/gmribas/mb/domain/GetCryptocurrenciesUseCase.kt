package com.gmribas.mb.domain

import androidx.paging.PagingData
import com.gmribas.mb.repository.cryptocurrency.ICryptocurrencyRepository
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCryptocurrenciesUseCase @Inject constructor(
    private val repository: ICryptocurrencyRepository
) {
    operator fun invoke(): Flow<PagingData<CryptocurrencyDTO>> {
        return repository.getCryptocurrenciesPagingData()
    }
    
    suspend fun getLatestListings(
        start: Int = 1,
        limit: Int = 20
    ): UseCaseResult<List<CryptocurrencyDTO>> {
        return try {
            val result = repository.getLatestListings(
                start = start,
                limit = limit
            )
            UseCaseResult.Success(result.cryptocurrencies)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}