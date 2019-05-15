package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.databinding.ActivityHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = MainPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.userHeaderContainer.startShimmer()

        uiScope.launch {
            delay(2000)
            userViewModel.getCurrentUser().observeForever {
                binding.user = it
                debugLog("Current User: $it")
            }
        }

        repository.getAllProducts().observeForever {
            debugLog("Products in DAO: $it")
        }
    }

    fun editProfile(view: View) {
        //todo: edit user's profile information
    }

    fun postIssue(view: View) = PostIssueFragment().show(supportFragmentManager, PostIssueFragment.FRAGMENT_TAG)

    private fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.zenitech_app_name)
            .setMessage(R.string.lorem_ipsum)
            .setPositiveButton("Next", null)
            .setNegativeButton("Cancel", null)
            .show()
    }

}