package favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.AssetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import model.Asset
import ui.BaseViewModel

class FavoriteViewModel(
    private val repository: AssetRepository
) : BaseViewModel(Dispatchers.IO) {

    val favoriteAssets: StateFlow<List<Asset>> = repository.getFavoriteAssetsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
