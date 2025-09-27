package com.gmribas.mb.ui.exchange.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.gmribas.mb.R
import com.gmribas.mb.core.extensions.formatAsPercentage
import com.gmribas.mb.core.extensions.formatAsUSD
import com.gmribas.mb.core.extensions.formatDateAdded
import com.gmribas.mb.core.extensions.formatVolume
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.ui.theme.AccentGreen
import com.gmribas.mb.ui.theme.AccentRed
import com.gmribas.mb.ui.theme.SIZE_4
import com.gmribas.mb.ui.theme.SIZE_12
import com.gmribas.mb.ui.theme.SIZE_40
import com.gmribas.mb.ui.theme.SIZE_80
import com.gmribas.mb.ui.theme.SPACING_8
import com.gmribas.mb.ui.theme.SPACING_12
import com.gmribas.mb.ui.theme.SPACING_16
import com.gmribas.mb.ui.theme.TextSecondary
import androidx.compose.foundation.isSystemInDarkTheme

private const val COIN_ICON_PATH = "https://s2.coinmarketcap.com/static/img/coins/64x64/"
@Composable
fun ExchangeItem(
    cryptocurrency: CryptocurrencyDTO,
    onClick: () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val secondaryTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val cardElevation = if (isDarkTheme) 2.dp else 4.dp
    
    val percentageColor = when {
        cryptocurrency.percentChange24h > 0 -> AccentGreen
        cryptocurrency.percentChange24h < 0 -> AccentRed
        else -> secondaryTextColor
    }
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(SIZE_80),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(SIZE_12)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SPACING_12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cryptocurrency Logo
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(COIN_ICON_PATH + "${cryptocurrency.id}.png")
                        .build(),
                    contentDescription = stringResource(
                        R.string.crypto_logo_description,
                        cryptocurrency.name
                    ),
                    modifier = Modifier
                        .size(SIZE_40)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(SPACING_12))
                
                // Name and Symbol
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = cryptocurrency.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = cryptocurrency.symbol,
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor
                        )
                        Text(
                            text = " • ",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor
                        )
                        Text(
                            text = cryptocurrency.dateAdded.formatDateAdded(),
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            
            // Price and Volume Info
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = cryptocurrency.price.formatAsUSD(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.volume_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = secondaryTextColor
                    )
                    Spacer(modifier = Modifier.width(SPACING_8 / 2))
                    Text(
                        text = "$${cryptocurrency.volume24h.formatVolume()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = secondaryTextColor
                    )
                }
                Text(
                    text = cryptocurrency.percentChange24h.formatAsPercentage(),
                    style = MaterialTheme.typography.bodySmall,
                    color = percentageColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
