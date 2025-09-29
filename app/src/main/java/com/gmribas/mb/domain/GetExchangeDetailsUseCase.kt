package com.gmribas.mb.domain

import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.exchange.IExchangeRepository
import javax.inject.Inject

class GetExchangeDetailsUseCase @Inject constructor(
    private val repository: IExchangeRepository
) {
    suspend operator fun invoke(id: Int): UseCaseResult<ExchangeDTO> {
        return try {
            val result = repository.getInfo(id)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}