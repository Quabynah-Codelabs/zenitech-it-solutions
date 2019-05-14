package io.codelabs.zenitech.core

import android.app.Application
import android.content.Context
import io.codelabs.zenitech.core.theme.PreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ZenitechApp : Application() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize Theme Preferences
        preferenceRepository = PreferenceRepository(
            getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        )

        startKoin {
            androidContext(this@ZenitechApp)
            modules(roomModule, prefsModule)
        }
    }

}