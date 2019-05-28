package io.codelabs.zenitech.core.datasource.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.core.USER_PREFS

class Preferences constructor(ctx: Context) {

    companion object {
        private const val USER_KEY = "user_key"
        private const val SHOW_UI = "show_ui"
        @Volatile
        private var instance: Preferences? = null

        @JvmStatic
        fun getInstance(context: Context): Preferences {
            return instance ?: synchronized(this) {
                return instance ?: Preferences(context).also { instance = it }
            }
        }
    }

    private val prefs: SharedPreferences = ctx.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean = false
    var isShowOnBoarding: Boolean = true
        get() = prefs.getBoolean(SHOW_UI, true)
        set(value) {
            field = value
            prefs.edit {
                debugLog("OnBoarding: $value")
                putBoolean(SHOW_UI, value)
                apply()
            }
        }

    var key: String? = null
        get() = prefs.getString(USER_KEY, null)
        set(value) {
            isLoggedIn = /*!key.isNullOrEmpty()*/ true
            prefs.edit {
                putString(USER_KEY, value)
                apply()
            }
            field = value
        }

    init {
        key = prefs.getString(USER_KEY, null)
        isShowOnBoarding = prefs.getBoolean(SHOW_UI, true)

        isLoggedIn = !key.isNullOrEmpty()
        if (isLoggedIn) key = prefs.getString(USER_KEY, null)
    }

}