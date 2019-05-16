package io.codelabs.zenitech.core

import android.app.Application
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import io.codelabs.zenitech.core.theme.PreferenceRepository
import io.codelabs.zenitech.core.worker.SyncedWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class ZenitechApp : Application() {

    lateinit var preferenceRepository: PreferenceRepository
//    private val prefs: Preferences by inject()
//    private val dao: RoomAppDao by inject()

    override fun onCreate() {
        super.onCreate()

        // Initialize Theme Preferences
        preferenceRepository = PreferenceRepository(
            getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        )

        startKoin {
            androidContext(this@ZenitechApp)
            modules(roomModule, prefsModule, remoteService)
        }

        val syncedWorker = PeriodicWorkRequestBuilder<SyncedWorker>(10, TimeUnit.MINUTES).build()

        WorkManager.getInstance(applicationContext).enqueue(syncedWorker)
    }


}