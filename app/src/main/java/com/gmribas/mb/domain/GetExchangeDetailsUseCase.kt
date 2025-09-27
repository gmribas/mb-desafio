package com.gmribas.mb.domain

import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import com.gmribas.mb.repository.exchange.IExchangeRepository
import javax.inject.Inject

class GetExchangeDetailsUseCase @Inject constructor(
    private val repository: IExchangeRepository
) {
    suspend operator fun invoke(exchangeId: Int): UseCaseResult<ExchangeDetailDTO> {
        return try {
            val result = repository.getExchangeDetails(exchangeId)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e as Throwable)
        }
    }
}