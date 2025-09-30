package com.gmribas.mb.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gmribas.mb.R
import com.gmribas.mb.ui.assets.model.AssetsListScreenEvent
import com.gmribas.mb.ui.assets.model.AssetsListScreenState
import com.gmribas.mb.ui.common.AssetRow
import com.gmribas.mb.ui.common.LoadingContent
import com.gmribas.mb.ui.common.TopAppBarMB
import com.gmribas.mb.ui.theme.SIZE_1
import com.gmribas.mb.ui.theme.SPACING_24
import com.gmribas.mb.ui.theme.SPACING_8

@Composable
fun AssetsListScreen(
    state: AssetsListScreenState,
    onEvent: (AssetsListScreenEvent) -> Unit,
    onBackClick: () -> Unit
) {

    LaunchedEffect(Unit) {
        onEvent(AssetsListScreenEvent.ProcessAssetsJson)
    }

    Scaffold(
        topBar = {
            TopAppBarMB(
                title = stringResource(R.string.assets_title),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.assets == null) {
                LoadingContent()
            }

            state.assets?.let { assets ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SPACING_24)
                ) {
                    items(assets.size) { index ->
                        AssetRow(asset = assets[index])
                        HorizontalDivider(thickness = SIZE_1)
                        Spacer(modifier = Modifier.height(SPACING_8))
                    }
                }
            }
        }
    }
}
