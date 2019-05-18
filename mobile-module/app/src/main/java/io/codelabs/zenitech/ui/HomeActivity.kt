package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.DEFAULT_AVATAR
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var isBackPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = MainPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.userHeaderContainer.startShimmer()

        uiScope.launch {
            userViewModel.getCurrentUser().observeForever {
                binding.user = it.apply {
                    if (this != null && avatar.isNullOrEmpty()) avatar = DEFAULT_AVATAR
                }.also { user ->
                    if (user != null) userViewModel.updateUser(user)
                }
                debugLog("Current User: $it")
            }

            uiScope.launch {
                debugLog(userViewModel.getAll())
            }
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

    override fun onBackPressed() {
        if (isBackPressed) super.onBackPressed() else {
            Snackbar.make(binding.container, "Press back again to exit", Snackbar.LENGTH_SHORT)
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        isBackPressed = false
                    }
                })
                .show()
            isBackPressed = true
        }
    }

}