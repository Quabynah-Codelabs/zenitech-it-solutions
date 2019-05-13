package io.codelabs.zenitech.ui

import android.os.Bundle
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.R

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}