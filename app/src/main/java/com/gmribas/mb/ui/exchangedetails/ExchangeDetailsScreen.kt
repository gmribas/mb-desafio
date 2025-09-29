package com.gmribas.mb.ui.exchangedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.gmribas.mb.R
import com.gmribas.mb.core.extensions.formatAsUSD
import com.gmribas.mb.core.extensions.formatDateAdded
import com.gmribas.mb.core.extensions.openBrowser
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.ui.common.ErrorContent
import com.gmribas.mb.ui.common.LoadingContent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import com.gmribas.mb.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsScreen(
    state: ExchangeDetailsScreenState,
    onEvent: (ExchangeDetailsScreenEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.exchange_details_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (state) {
                is ExchangeDetailsScreenState.Loading -> {
                    LoadingContent(modifier = Modifier.fillMaxSize())
                }

                is ExchangeDetailsScreenState.Error -> {
                    ErrorContent(
                        error = state.message,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is ExchangeDetailsScreenState.Success -> {
                    ExchangeDetailsContent(
                        exchange = state.exchange,
                        assets = state.assets,
                        assetsLoading = state.assetsLoading,
                        onWebsiteClick = { url ->
                            context.openBrowser(url)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExchangeDetailsContent(
    exchange: ExchangeDTO,
    assets: List<ExchangeAssetDTO>,
    assetsLoading: Boolean,
    onWebsiteClick: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(SPACING_16),
        verticalArrangement = Arrangement.spacedBy(SPACING_16)
    ) {
        // Header Card with Logo and Name
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = SIZE_4)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SPACING_20),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                exchange.logo?.let { logoUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(logoUrl)
                            .build(),
                        contentDescription = stringResource(
                            R.string.exchange_logo_description,
                            exchange.name.orEmpty()
                        ),
                        modifier = Modifier
                            .size(SIZE_80)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(SPACING_12))
                }

                // Name
                Text(
                    text = exchange.name.orEmpty(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        AssetsCard(
            assets = assets,
            isLoading = assetsLoading
        )

        // Description Card
        exchange.description?.let { description ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = SIZE_4)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SPACING_16)
                ) {
                    Text(
                        text = stringResource(R.string.description_label),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(SPACING_8))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = SIZE_4)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SPACING_16),
                verticalArrangement = Arrangement.spacedBy(SPACING_12)
            ) {
                Text(
                    text = stringResource(R.string.details_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                exchange.urls?.let {
                    DetailRow(
                        label = stringResource(R.string.website_label),
                        value = it.website,
                        isClickable = true,
                        onClick = { onWebsiteClick(it.website) }
                    )

                    DetailRow(
                        label = stringResource(R.string.chat_label),
                        value = it.chat,
                        isClickable = true,
                        onClick = { onWebsiteClick(it.chat) }
                    )

                    DetailRow(
                        label = stringResource(R.string.fee_label),
                        value = it.fee,
                        isClickable = true,
                        onClick = { onWebsiteClick(it.fee) }
                    )

                    DetailRow(
                        label = stringResource(R.string.twitter_label),
                        value = it.twitter,
                        isClickable = true,
                        onClick = { onWebsiteClick(it.twitter) }
                    )
                }

                // Date Launched
                DetailRow(
                    label = stringResource(R.string.date_launched_label),
                    value = exchange.dateLaunched?.formatDateAdded()
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String?,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    value?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isClickable) Modifier.clickable { onClick() } else Modifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isClickable) AccentGreen else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isClickable) FontWeight.Medium else FontWeight.Normal,
                textDecoration = if (isClickable) TextDecoration.Underline else TextDecoration.None,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AssetsCard(
    assets: List<ExchangeAssetDTO>,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = SIZE_4)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SPACING_16),
            verticalArrangement = Arrangement.spacedBy(SPACING_12)
        ) {
            Text(
                text = stringResource(R.string.assets_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            when {
                isLoading -> {
                    LoadingContent(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                assets.isNotEmpty() -> {
                    assets.take(5).forEach { asset ->
                        AssetRow(asset = asset)
                    }
                }

                else -> {
                    Text(
                        text = stringResource(R.string.no_assets_available),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetRow(
    asset: ExchangeAssetDTO
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
