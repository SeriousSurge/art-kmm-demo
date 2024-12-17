package database

import androidx.room.Database
import androidx.room.RoomDatabase
import database.dao.AssetDao
import database.dao.SyncEventDao
import database.entities.AssetEntity
import database.entities.SyncEvent

@Database(
    entities = [AssetEntity::class, SyncEvent::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase(), DB {

    abstract fun assetDao(): AssetDao
    abstract fun syncEventDao(): SyncEventDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// TODO: Added a hack to resolve below issue:
// Class is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
    fun clearAllTables() {}
}