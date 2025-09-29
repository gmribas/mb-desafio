package com.gmribas.mb.domain

import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.exchange.IExchangeRepository
import javax.inject.Inject

class GetExchangeAssetsUseCase @Inject constructor(
    private val repository: IExchangeRepository
) {
    suspend operator fun invoke(exchangeId: Int): UseCaseResult<List<ExchangeAssetDTO>> {
        return try {
            val assets = repository.getExchangeAssets(exchangeId)
            UseCaseResult.Success(assets)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}