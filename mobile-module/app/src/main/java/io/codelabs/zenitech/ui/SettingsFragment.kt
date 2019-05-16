package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.USER_VM
import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.core.datasource.viewmodel.UserViewModel
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.viewModel

class SettingsFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val userViewModel: UserViewModel by viewModel(USER_VM)
    private val prefs: Preferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            debugLog("Settings User: $it")
            debugLog("Login state: ${prefs.key}")
            binding.user = it
        })

        binding.logoutButton.setOnClickListener {
            prefs.key = null
            userViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    userViewModel.removeUser(it)
                    requireActivity().intentTo(MainActivity::class.java, true)
                } else {
                    Snackbar.make(binding.root, "Please sign in first", Snackbar.LENGTH_LONG).show()
                }
            })
        }
        /*val preferenceRepository = (requireActivity().application as ZenitechApp).preferenceRepository

        preferenceRepository.isDarkThemeLive.observe(this, Observer { isDarkTheme ->
            isDarkTheme?.let { binding.darkThemeSwitch.isChecked = it }
        })

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            preferenceRepository.isDarkTheme = checked
        }*/
    }

}