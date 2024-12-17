package data.repository

enum class GroupMode {
    NONE,
    ARTIST,
    PRODUCTION_PLACE,
    MATERIAL
}

enum class SortMode {
    BY_TITLE,
    BY_ARTIST
}

data class FilterState(
    val mode: FilterMode = FilterMode.BY_TITLE,
    val query: String = "",
    val selectedArtist: String? = null,
    val selectedCategory: String? = null,
    val groupMode: GroupMode = GroupMode.NONE,
    val selectedYear: Int? = null, // if we ever had years
    val selectedProductionPlace: String? = null,
    val selectedMaterial: String? = null,
    val sortMode: SortMode = SortMode.BY_TITLE
)

enum class FilterMode {
    BY_TITLE,
    BY_ARTIST,
    BY_CATEGORY
}
