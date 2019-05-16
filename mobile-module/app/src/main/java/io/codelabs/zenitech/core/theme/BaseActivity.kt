package io.codelabs.zenitech.core.theme

import android.os.Bundle
import androidx.lifecycle.Observer
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.view.BaseActivity
import io.codelabs.zenitech.core.USER_VM
import io.codelabs.zenitech.core.ZenitechApp
import io.codelabs.zenitech.core.auth.AuthService
import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.datasource.viewmodel.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.viewModel

abstract class BaseActivity : BaseActivity() {
    val prefs: Preferences by inject()
    val repository: ProductRepository by inject()
    val userViewModel: UserViewModel by viewModel(USER_VM)
    val authService: AuthService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ZenitechApp).preferenceRepository.nightModeLive.observeForever { nightMode ->
            nightMode?.let { delegate.localNightMode = it }
        }

        (application as ZenitechApp).preferenceRepository.isDarkThemeLive.observeForever {
            debugLog("Dark Theme: $it")
        }
    }

}