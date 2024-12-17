package list

import androidx.lifecycle.viewModelScope
import data.repository.AssetRepository
import data.repository.FilterMode
import data.repository.FilterState
import data.repository.GroupMode
import data.repository.SortMode
import data.repository.SyncResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import model.Asset
import ui.BaseViewModel

class AssetListViewModel(private val repository: AssetRepository) : BaseViewModel(Dispatchers.IO) {
    private val _allAssets = repository.getAssetsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _filterState = MutableStateFlow(FilterState())

    private val _filteredAssets = MutableStateFlow<List<Asset>>(emptyList())
    val assets: StateFlow<List<Asset>> = _filteredAssets

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    init {
        initializeData()

        viewModelScope.launch {
            // Re-compute filtered & sorted assets whenever _allAssets or _filterState changes
            combine(_allAssets, _filterState) { all, filter ->
                val filtered = filterList(all, filter)
                applySort(filtered, filter.sortMode)
            }.collect { sortedList ->
                _filteredAssets.value = sortedList
            }
        }
    }

    private fun initializeData() {
        viewModelScope.launch {
            synchronizeAssets()
        }
    }

    fun synchronizeAssets() {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = repository.synchronizeAssets()
            _isSyncing.value = false
            if (result.error != null) {
                _error.value = result.error
            } else {
                _error.value = null
            }
        }
    }

    fun loadMore() {
        // Trigger loading the next page
        viewModelScope.launch {
            showLoader(true)
            val result = repository.loadNextPage()
            showLoader(false)
            if (result.error != null) {
                _error.value = result.error
            }
        }
    }

    fun searchArtworks(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    private fun filterList(all: List<Asset>, filter: FilterState): List<Asset> {
        val baseFiltered = when (filter.mode) {
            FilterMode.BY_TITLE -> {
                if (filter.query.isBlank()) all
                else all.filter {
                    it.title.contains(filter.query, ignoreCase = true) ||
                            (it.principalMaker?.contains(filter.query, ignoreCase = true) == true)
                }
            }
            FilterMode.BY_ARTIST -> {
                if (filter.selectedArtist == null) all
                else all.filter { it.principalMaker == filter.selectedArtist }
            }
            FilterMode.BY_CATEGORY -> {
                if (filter.selectedCategory == null) all
                else all.filter { it.materials?.contains(filter.selectedCategory) == true }
            }
        }

        val grouped = when (filter.groupMode) {
            GroupMode.NONE -> baseFiltered
            GroupMode.ARTIST -> {
                if (filter.selectedArtist == null) baseFiltered
                else baseFiltered.filter { it.principalMaker == filter.selectedArtist }
            }
            GroupMode.PRODUCTION_PLACE -> {
                if (filter.selectedProductionPlace == null) {
                    baseFiltered
                } else {
                    baseFiltered.filter {
                        it.productionPlaces?.contains(filter.selectedProductionPlace) == true
                    }
                }
            }
            GroupMode.MATERIAL -> {
                // Similar logic if you want to group by material
                if (filter.selectedMaterial == null) {
                    baseFiltered
                } else {
                    baseFiltered.filter { it.materials?.contains(filter.selectedMaterial) == true }
                }
            }
        }

        return grouped
    }

    private fun applySort(list: List<Asset>, sortMode: SortMode): List<Asset> {
        return when (sortMode) {
            SortMode.BY_TITLE -> list.sortedBy { it.title }
            SortMode.BY_ARTIST -> list.sortedBy { it.principalMaker ?: "" }
        }
    }
}

