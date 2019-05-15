package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.DEFAULT_AVATAR
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.data.User
import io.codelabs.zenitech.databinding.ActivityHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = MainPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = adapter

        ioScope.launch {
            val liveData = userViewModel.getCurrentUser()

            uiScope.launch {
                liveData.observeForever {
                    binding.user = it
                    debugLog("Current User: $it")
                }
            }
        }

        uiScope.launch {
            repository.getAllProducts().observeForever {
                debugLog("Products in DAO: $it")
            }
        }
    }

    fun editProfile(view: View) {}

    fun postIssue(view: View) {}


}