package com.gmribas.mb.ui.exchange.components

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.gmribas.mb.R
import com.gmribas.mb.core.extensions.formatAsUSD
import com.gmribas.mb.core.extensions.formatDateAdded
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.ui.theme.SIZE_12
import com.gmribas.mb.ui.theme.SIZE_40
import com.gmribas.mb.ui.theme.SIZE_80
import com.gmribas.mb.ui.theme.SPACING_12

@Composable
fun ExchangeItem(
    exchange: ExchangeDTO,
    onClick: () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val secondaryTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val cardElevation = if (isDarkTheme) 2.dp else 4.dp
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .testTag("ExchangeItem")
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
                        .data(exchange.logo)
                        .build(),
                    contentDescription = stringResource(
                        R.string.crypto_logo_description,
                        exchange.name.orEmpty()
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
                        text = exchange.name.orEmpty(),
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
                            text = stringResource(R.string.date_added),
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor
                        )
                        Text(
                            text = " • ",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor
                        )
                        Text(
                            text = exchange.dateLaunched?.formatDateAdded().orEmpty(),
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
                    text = exchange.spotVolumeUsd?.formatAsUSD() ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
