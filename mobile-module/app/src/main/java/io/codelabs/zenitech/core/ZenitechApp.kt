package io.codelabs.zenitech.core

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.core.theme.PreferenceRepository
import io.codelabs.zenitech.core.worker.SyncedWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class ZenitechApp : Application() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize Theme Preferences
        preferenceRepository = PreferenceRepository(
            getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        )

        // Initialize Firebase SDK
        FirebaseApp.initializeApp(this).also {
            debugLog("Firebase SDK initialized as: ${it?.name}")
        }

        startKoin {
            androidContext(this@ZenitechApp)
            modules(roomModule, prefsModule, remoteService, firebaseModule)
        }

        // Set constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncedWorker = PeriodicWorkRequestBuilder<SyncedWorker>(10, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(mutableListOf(syncedWorker))
    }


}