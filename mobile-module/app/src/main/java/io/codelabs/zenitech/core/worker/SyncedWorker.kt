package io.codelabs.zenitech.core.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncedWorker constructor(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    override fun doWork(): Result {
        
    }
}