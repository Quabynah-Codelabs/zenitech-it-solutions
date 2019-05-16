package io.codelabs.zenitech.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.codelabs.zenitech.core.database.DatabaseService
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.core.datasource.room.RoomAppDatabase

class SyncedWorker constructor(
    context: Context,
    parameters: WorkerParameters
) :
    CoroutineWorker(context, parameters) {

    private val dao: RoomAppDao by lazy { RoomAppDatabase.get(context).dao() }
    private val api: DatabaseService by lazy { DatabaseService.get() }

    override suspend fun doWork(): Result {
        val issues = dao.getLiveIssues()
        val products = dao.getLiveProducts()
        api.syncDataWithServer(dao, issues, products)
        return Result.success()
    }

}