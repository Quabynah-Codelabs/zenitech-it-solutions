package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.View
import io.codelabs.sdk.util.intentTo
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navHome(v: View?) = intentTo(HomeActivity::class.java)
}
