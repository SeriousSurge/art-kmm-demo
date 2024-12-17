package database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import database.entities.SyncEvent

@Dao
interface SyncEventDao {
    @Query("SELECT * FROM sync_events ORDER BY timestamp DESC")
    suspend fun getSyncHistory(): List<SyncEvent>

    @Insert
    suspend fun insertSyncEvent(syncEvent: SyncEvent)
}