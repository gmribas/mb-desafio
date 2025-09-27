package com.gmribas.mb.repository.mapper

import com.gmribas.mb.core.IMapper
import com.gmribas.mb.data.model.Cryptocurrency
import com.gmribas.mb.data.model.CryptocurrencyListingResponse
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.repository.dto.CryptocurrencyListingDTO

class CryptocurrencyMapper : IMapper<Cryptocurrency, CryptocurrencyDTO> {
    
    override fun toDTO(model: Cryptocurrency): CryptocurrencyDTO {
        val usdQuote = model.quote["USD"]
        return CryptocurrencyDTO(
            id = model.id,
            name = model.name,
            symbol = model.symbol,
            slug = model.slug,
            rank = model.cmcRank,
            price = usdQuote?.price ?: 0.0,
            volume24h = usdQuote?.volume24h ?: 0.0,
            marketCap = usdQuote?.marketCap ?: 0.0,
            percentChange1h = usdQuote?.percentChange1h ?: 0.0,
            percentChange24h = usdQuote?.percentChange24h ?: 0.0,
            percentChange7d = usdQuote?.percentChange7d ?: 0.0,
            circulatingSupply = model.circulatingSupply,
            totalSupply = model.totalSupply,
            maxSupply = model.maxSupply,
            lastUpdated = model.lastUpdated,
            dateAdded = model.dateAdded
        )
    }

}

class CryptocurrencyListingMapper(
    private val cryptocurrencyMapper: CryptocurrencyMapper
) : IMapper<CryptocurrencyListingResponse, CryptocurrencyListingDTO> {
    
    override fun toDTO(model: CryptocurrencyListingResponse): CryptocurrencyListingDTO {
        return CryptocurrencyListingDTO(
            cryptocurrencies = model.data.map { cryptocurrencyMapper.toDTO(it) },
            totalCount = model.status.totalCount
        )
    }

}