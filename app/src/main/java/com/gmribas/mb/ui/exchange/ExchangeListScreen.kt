package com.gmribas.mb.ui.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.activity.compose.BackHandler
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.gmribas.mb.R
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.ui.common.ErrorContent
import com.gmribas.mb.ui.common.LoadingContent
import com.gmribas.mb.ui.exchange.components.ExchangeItem
import com.gmribas.mb.ui.exchange.model.ExchangeListScreenState
import com.gmribas.mb.ui.theme.SPACING_16
import com.gmribas.mb.ui.theme.SPACING_20
import com.gmribas.mb.ui.theme.SPACING_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeListScreen(
    state: ExchangeListScreenState,
    onItemClick: (CryptocurrencyDTO) -> Unit,
    onFinish: () -> Unit,
) {
    BackHandler {
        onFinish()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = SPACING_24, vertical = SPACING_20)
            ) {
                Text(
                    text = stringResource(R.string.exchange_title),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            when (state) {
                is ExchangeListScreenState.ExchangeScreenLoadingState -> {
                    LoadingContent(modifier = Modifier.fillMaxSize())
                }
                
                is ExchangeListScreenState.ExchangeScreenErrorState -> {
                    val errorState = state as ExchangeListScreenState.ExchangeScreenErrorState
                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = {},
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ErrorContent(
                            error = errorState.error,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                
                is ExchangeListScreenState.ExchangeScreenSuccessState -> {
                    val successState = state as ExchangeListScreenState.ExchangeScreenSuccessState
                    val cryptocurrenciesPagingItems = successState.cryptocurrenciesPagingFlow.collectAsLazyPagingItems()
                    
                    var isUserRefreshing by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(cryptocurrenciesPagingItems.loadState.refresh) {
                        if (cryptocurrenciesPagingItems.loadState.refresh !is LoadState.Loading) {
                            isUserRefreshing = false
                        }
                    }
                    
                    PullToRefreshBox(
                        isRefreshing = isUserRefreshing && cryptocurrenciesPagingItems.loadState.refresh is LoadState.Loading,
                        onRefresh = {
                            isUserRefreshing = true
                            cryptocurrenciesPagingItems.refresh()
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (cryptocurrenciesPagingItems.loadState.refresh) {
                            is LoadState.Loading -> {
                                LoadingContent(modifier = Modifier.fillMaxSize())
                            }
                            is LoadState.Error -> {
                                val error = cryptocurrenciesPagingItems.loadState.refresh as LoadState.Error
                                ErrorContent(
                                    error = stringResource(R.string.error_prefix) + error.error.localizedMessage,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            else -> {
                                when(cryptocurrenciesPagingItems.loadState.append) {
                                    is LoadState.Loading -> {
                                            LoadingContent(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                            )
                                    }
                                    is LoadState.Error -> {
                                        val error = cryptocurrenciesPagingItems.loadState.append as LoadState.Error
                                        ErrorContent(
                                            error = stringResource(R.string.error_prefix) + error.error.localizedMessage,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    else -> {}
                                }

                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(SPACING_16),
                                    contentPadding = PaddingValues(horizontal = SPACING_16),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        count = cryptocurrenciesPagingItems.itemCount,
                                        key = { index -> cryptocurrenciesPagingItems[index]?.id ?: index }
                                    ) { index ->
                                        val cryptocurrency = cryptocurrenciesPagingItems[index]
                                        cryptocurrency?.let {
                                            ExchangeItem(
                                                cryptocurrency = it,
                                                onClick = { onItemClick(it) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                is ExchangeListScreenState.ExchangeScreenIdleState -> {}
            }
        }
    }
}