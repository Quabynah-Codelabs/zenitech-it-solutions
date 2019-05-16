package io.codelabs.zenitech.core.database

import android.text.format.DateUtils
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.core.util.markAsSynced
import io.codelabs.zenitech.data.Issue
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.SyncableDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseService {

    companion object {
        @Volatile
        private var instance: DatabaseService? = null

        fun get(): DatabaseService {
            return instance ?: synchronized(this) {
                instance ?: DatabaseService().also { instance = it }
            }
        }
    }

    suspend fun syncDataWithServer(dao: RoomAppDao, vararg models: MutableList<out SyncableDataModel>) {
        var timeStarted = System.currentTimeMillis()
        debugLog("Data synced started at $timeStarted")
        withContext(Dispatchers.IO) {
            models.forEach { _models ->
                _models.markAsSynced()
                _models.forEach {
                    when (it) {
                        is Product -> dao.addProduct(it)
                        is Issue -> dao.addIssue(it)
                    }
                }
            }
        }

        debugLog(
            "Data synced finished within ${DateUtils.getRelativeTimeSpanString(
                timeStarted,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )}"
        )
    }

}