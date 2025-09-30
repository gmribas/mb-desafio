package com.gmribas.mb.ui.assets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.ui.assets.model.AssetsListScreenEvent
import com.gmribas.mb.ui.assets.model.AssetsListScreenState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@HiltViewModel
class AssetsListScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<AssetsListScreenState> = MutableStateFlow(AssetsListScreenState())
    val state: StateFlow<AssetsListScreenState> = _state.asStateFlow()

    fun onEvent(event: AssetsListScreenEvent) {
        when (event) {
            is AssetsListScreenEvent.ProcessAssetsJson -> processAssetsJson()
        }
    }

    internal fun processAssetsJson() = viewModelScope.launch {
        val assetsJson = savedStateHandle.get<String>("assetsJson")
        assetsJson?.let {
            val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            val type = object : TypeToken<List<ExchangeAssetDTO>>() {}.type
            _state.value = _state.value.copy(assets = Gson().fromJson(decoded, type))
        }
    }
}
