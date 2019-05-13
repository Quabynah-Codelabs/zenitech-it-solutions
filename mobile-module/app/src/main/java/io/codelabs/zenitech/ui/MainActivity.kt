package io.codelabs.zenitech.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.codelabs.zenitech.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Disable night mode by default
//        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    }
}
