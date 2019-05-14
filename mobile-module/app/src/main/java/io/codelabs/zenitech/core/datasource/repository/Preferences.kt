package io.codelabs.zenitech.core.datasource.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.codelabs.zenitech.core.USER_PREFS

class Preferences constructor(ctx: Context) {

    companion object {
        private const val USER_KEY = "user_key"
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
    var key: String? = null
        get() = prefs.getString(USER_KEY, null)
        set(value) {
            field = value

            isLoggedIn = !key.isNullOrEmpty()
            prefs.edit {
                putString(USER_KEY, value)
                commit()
            }
        }

    init {
        key = prefs.getString(USER_KEY, null)
        isLoggedIn = !key.isNullOrEmpty()
        if (isLoggedIn) key = prefs.getString(USER_KEY, null)
    }

}