package io.codelabs.zenitech.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = MainPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = adapter


    }
}