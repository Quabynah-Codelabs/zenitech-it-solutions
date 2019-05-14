package io.codelabs.zenitech.core.theme

import android.os.Bundle
import io.codelabs.sdk.view.BaseActivity
import io.codelabs.zenitech.core.ZenitechApp

abstract class BaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ZenitechApp).preferenceRepository.nightModeLive.observeForever { nightMode ->
            nightMode?.let { delegate.localNightMode = it }
        }
    }

}