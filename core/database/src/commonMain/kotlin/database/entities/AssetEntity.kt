package database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val isFavorite: Boolean
)
