package io.codelabs.zenitech.core

import android.app.Application
import android.content.Context
import io.codelabs.zenitech.core.theme.PreferenceRepository

class ZenitechApp : Application() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()
        preferenceRepository = PreferenceRepository(
            getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        )
    }

}