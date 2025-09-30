package com.gmribas.mb.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.gmribas.mb.core.extensions.formatAsUSD
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.ui.theme.AccentGreen
import com.gmribas.mb.ui.theme.SIZE_36
import com.gmribas.mb.ui.theme.SPACING_8

@Composable
fun AssetRow(asset: ExchangeAssetDTO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SPACING_8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(asset.logo)
                .build(),
            contentDescription = asset.name,
            modifier = Modifier
                .size(SIZE_36)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(SPACING_8))

        Text(
            modifier = Modifier.weight(1f),
            text = asset.currency?.name.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )

        val priceAndSymbol = remember {
            asset.symbol.orEmpty() + asset.currency?.priceUsd?.formatAsUSD()
        }

        Text(
            text = priceAndSymbol,
            style = MaterialTheme.typography.bodyMedium,
            color = AccentGreen,
            fontWeight = FontWeight.SemiBold
        )
    }
}
