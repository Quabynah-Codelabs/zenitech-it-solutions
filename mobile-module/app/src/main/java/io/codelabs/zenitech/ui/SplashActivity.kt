package io.codelabs.zenitech.ui

import android.os.Bundle
import io.codelabs.sdk.util.setupSplashTransition
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Create a splash screen setup
        setupSplashTransition(
            if (prefs.isLoggedIn) {
                if (prefs.isShowOnBoarding) {
                    WelcomeActivity::class.java
                } else {
                    HomeActivity::class.java
                }
            } else AuthActivity::class.java
        )
    }
}