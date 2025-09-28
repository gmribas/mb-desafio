package com.gmribas.mb.domain

import com.gmribas.mb.repository.dto.CriptoDetailDTO
import com.gmribas.mb.repository.cryptocurrency.ICriptoRepository
import com.gmribas.mb.domain.UseCaseResult
import javax.inject.Inject

class GetCriptoDetailsUseCase @Inject constructor(
    private val repository: ICriptoRepository
) {
    suspend operator fun invoke(exchangeId: Int): UseCaseResult<CriptoDetailDTO> {
        return try {
            val result = repository.getCriptoDetails(exchangeId)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e as Throwable)
        }
    }
}