package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.USER_VM
import io.codelabs.zenitech.core.ZenitechApp
import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.core.datasource.viewmodel.UserViewModel
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.preference_view.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            /*userViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    prefs.key = null
                    userViewModel.removeUser(it)
                    Snackbar.make(binding.root, "Signing you out...", Snackbar.LENGTH_INDEFINITE).show()

                    uiScope.launch {
                        delay(2000)
                        requireActivity().intentTo(MainActivity::class.java, true)
                    }
                } else {
                    Snackbar.make(binding.root, "Please sign in first", Snackbar.LENGTH_LONG).show()
                }
            })*/
            prefs.key = null
            Snackbar.make(binding.root, "Signing you out...", Snackbar.LENGTH_INDEFINITE).show()

            uiScope.launch {
                delay(2000)
                requireActivity().intentTo(MainActivity::class.java, true)
            }
        }

        // Show OnBoarding
        binding.uiPrefs.widgetFrame.findViewById<SwitchMaterial>(R.id.core_switch)?.apply {
            isChecked = prefs.isShowOnBoarding
            isUseMaterialThemeColors = true
            setOnCheckedChangeListener { _, isChecked ->
                prefs.isShowOnBoarding = isChecked
            }
        }

        // Night Mode
        binding.uiDarkMode.widgetFrame.findViewById<SwitchMaterial>(R.id.core_switch_theme)?.apply {
            (requireActivity().application as ZenitechApp).preferenceRepository.isDarkThemeLive.observe(
                viewLifecycleOwner,
                Observer {
                    isChecked = it
                })
            isUseMaterialThemeColors = true
            setOnCheckedChangeListener { _, isChecked ->
                (requireActivity().application as ZenitechApp).preferenceRepository.isDarkTheme = isChecked
            }
        }

    }

}