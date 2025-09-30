package com.gmribas.mb.ui.assets.model

sealed interface AssetsListScreenEvent {

    data object ProcessAssetsJson : AssetsListScreenEvent
}
