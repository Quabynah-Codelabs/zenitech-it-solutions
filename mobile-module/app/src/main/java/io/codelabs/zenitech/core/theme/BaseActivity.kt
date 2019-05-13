package io.codelabs.zenitech.core.theme

import io.codelabs.sdk.view.BaseActivity
import io.codelabs.zenitech.core.ZenitechApp

abstract class BaseActivity : BaseActivity() {

    override fun onStart() {
        super.onStart()

        (application as ZenitechApp).preferenceRepository.nightModeLive.observeForever { nightMode ->
            nightMode?.let { delegate.localNightMode = it }
        }
    }

}