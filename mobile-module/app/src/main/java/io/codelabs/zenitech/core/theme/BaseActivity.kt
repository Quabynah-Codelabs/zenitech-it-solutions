package io.codelabs.zenitech.core.theme

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.view.BaseActivity
import io.codelabs.zenitech.core.NEWSLETTERS_SUBSCRIPTION_TOPIC
import io.codelabs.zenitech.core.PRODUCT_VM
import io.codelabs.zenitech.core.USER_VM
import io.codelabs.zenitech.core.ZenitechApp
import io.codelabs.zenitech.core.auth.AuthService
import io.codelabs.zenitech.core.database.DatabaseAPI
import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.datasource.viewmodel.ProductViewModel
import io.codelabs.zenitech.core.datasource.viewmodel.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.viewModel

abstract class BaseActivity : BaseActivity() {
    val prefs: Preferences by inject()
    val repository: ProductRepository by inject()
    val api: DatabaseAPI by inject()
    val userViewModel: UserViewModel by viewModel(USER_VM)
    val productViewModel: ProductViewModel by viewModel(PRODUCT_VM)
    val authService: AuthService by inject()
    val messaging: FirebaseMessaging by inject()
    val auth: FirebaseAuth by inject()
    val storage: StorageReference by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ZenitechApp).preferenceRepository.nightModeLive.observeForever { nightMode ->
            debugLog("Night mode: $nightMode")
            nightMode?.let { delegate.localNightMode = it }
        }

        (application as ZenitechApp).preferenceRepository.isDarkThemeLive.observeForever {
            debugLog("Dark Theme: $it")
        }

        // Subscribe logged in users to newsletters
        if (prefs.isLoggedIn) messaging.subscribeToTopic(NEWSLETTERS_SUBSCRIPTION_TOPIC).addOnCompleteListener {
            debugLog("Subscribed to newsletters: ${it.isSuccessful}")
        }.addOnFailureListener {
            debugLog("Failed to subscribe to newsletter.\n${it.localizedMessage}")
        }
    }

}