package com.gmribas.mb.ui.exchangedetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.gmribas.mb.R
import com.gmribas.mb.core.extensions.formatDateAdded
import com.gmribas.mb.repository.dto.ExchangeDetailDTO
import com.gmribas.mb.ui.common.ErrorContent
import com.gmribas.mb.ui.common.LoadingContent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import com.gmribas.mb.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsScreen(
    exchangeId: Int,
    state: ExchangeDetailsScreenState,
    onEvent: (ExchangeDetailsScreenEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(exchangeId) {
        onEvent(ExchangeDetailsScreenEvent.LoadExchangeDetails(exchangeId))
    }

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
                            imageVector = Icons.Default.ArrowBack,
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
                        onWebsiteClick = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExchangeDetailsContent(
    exchange: ExchangeDetailDTO,
    onWebsiteClick: (String) -> Unit
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
                            exchange.name
                        ),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(SPACING_12))
                }
                
                // Name
                Text(
                    text = exchange.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                // Symbol
                Text(
                    text = exchange.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                
                // Category
                exchange.category?.let { category ->
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
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
                
                // Website
                exchange.websiteUrl?.let { url ->
                    DetailRow(
                        label = stringResource(R.string.website_label),
                        value = url,
                        isClickable = true,
                        onClick = { onWebsiteClick(url) }
                    )
                }
                
                // Date Added
                exchange.dateAdded?.let { date ->
                    DetailRow(
                        label = stringResource(R.string.date_added_label),
                        value = date.formatDateAdded()
                    )
                }
                
                // Date Launched
                exchange.dateLaunched?.let { date ->
                    DetailRow(
                        label = stringResource(R.string.date_launched_label),
                        value = date.formatDateAdded()
                    )
                }
                
                // Platform
                exchange.platform?.let { platform ->
                    DetailRow(
                        label = stringResource(R.string.platform_label),
                        value = platform
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
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