package com.gmribas.mb.ui.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.testTag
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.gmribas.mb.R
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.ui.common.ErrorContent
import com.gmribas.mb.ui.common.LoadingContent
import com.gmribas.mb.ui.exchange.components.ExchangeItem
import com.gmribas.mb.ui.theme.SPACING_16
import com.gmribas.mb.ui.theme.SPACING_20
import com.gmribas.mb.ui.theme.SPACING_24
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeListScreen(
    exchangesPagingFlow: Flow<PagingData<ExchangeDTO>>,
    onItemClick: (ExchangeDTO) -> Unit,
    onFinish: () -> Unit,
) {
    BackHandler {
        onFinish()
    }

    val lazyPagingItems = exchangesPagingFlow.collectAsLazyPagingItems()
    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = SPACING_24, vertical = SPACING_20)
        ) {
            Text(
                modifier = Modifier.testTag("title"),
                text = stringResource(R.string.exchange_title),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { lazyPagingItems.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount == 0 -> {
                    LoadingContent(modifier = Modifier.fillMaxSize())
                }

                lazyPagingItems.loadState.refresh is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
                    val error = lazyPagingItems.loadState.refresh as LoadState.Error
                    ErrorContent(
                        error = error.error.localizedMessage,
                        retry = true,
                        onRetry = { lazyPagingItems.retry() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(SPACING_16),
                        contentPadding = PaddingValues(horizontal = SPACING_16),
                        modifier = Modifier.testTag("LazyColumn").fillMaxSize()
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index ->
                                "${lazyPagingItems.peek(index)?.id}-$index"
                            }
                        ) { index ->
                            val cryptocurrency = lazyPagingItems[index]
                            cryptocurrency?.let {
                                ExchangeItem(
                                    exchange = it,
                                    onClick = { onItemClick(it) }
                                )
                            }
                        }

                        item {
                            when (lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    LoadingContent(
                                        modifier = Modifier
                                            .testTag("loading")
                                            .fillMaxWidth()
                                            .padding(vertical = SPACING_16)
                                    )
                                }
                                is LoadState.Error -> {
                                    val error = lazyPagingItems.loadState.append as LoadState.Error
                                    ErrorContent(
                                        error = error.error.localizedMessage,
                                        retry = true,
                                        onRetry = { lazyPagingItems.retry() },
                                        modifier = Modifier.fillMaxWidth().padding(SPACING_16)
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}