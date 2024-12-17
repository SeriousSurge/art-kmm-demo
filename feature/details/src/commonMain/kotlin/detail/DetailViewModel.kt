package detail

import androidx.lifecycle.viewModelScope
import common.result.Result
import data.repository.AssetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Asset
import ui.BaseViewModel

class AssetDetailViewModel(
    private val repository: AssetRepository
) : BaseViewModel(Dispatchers.IO) {

    private val _asset = MutableStateFlow<Asset?>(null)
    val asset: StateFlow<Asset?> = _asset

    fun loadAsset(objectNumber: String) {
        viewModelScope.launch {
            showLoader(true)
            val result = repository.getAssetDetails(objectNumber)
            showLoader(false)
            if (result is common.result.Result.Success) {
                _asset.value = result.data
            } else {
                fireError((result as common.result.Result.Error).exception)
            }
        }
    }

    fun toggleFavorite(asset: Asset) {
        viewModelScope.launch {
            repository.favorite(asset)
            _asset.value = asset.copy(isFavorite = !asset.isFavorite)
        }
    }
}