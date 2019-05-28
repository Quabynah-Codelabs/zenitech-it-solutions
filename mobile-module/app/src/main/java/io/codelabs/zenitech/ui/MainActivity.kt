package io.codelabs.zenitech.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.sdk.util.showConfirmationToast
import io.codelabs.sdk.util.toast
import io.codelabs.zenitech.BuildConfig
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.dbutil.LoginRequest
import io.codelabs.zenitech.core.dbutil.OAuthRequest
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.core.util.isNotEmpty
import io.codelabs.zenitech.data.User
import io.codelabs.zenitech.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.browse

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authSnackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)


        binding.loading.visibility = View.GONE
        authSnackbar = Snackbar.make(
            container,
            "Please wait while we sign you in...",
            Snackbar.LENGTH_INDEFINITE
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_google_login -> googleLogin()

            R.id.menu_reset_password -> {
                //todo: add password reset functionality
            }

            R.id.menu_about -> browse(BuildConfig.WEB_HOSTING_SITE, true)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEnterAnimationComplete() {
        if (prefs.isLoggedIn && !prefs.key.isNullOrEmpty()) showHomePrompt()
    }

    private fun showHomePrompt() {
        TransitionManager.beginDelayedTransition(binding.container)
        binding.loading.visibility = View.GONE
        binding.content.visibility = View.GONE
        userViewModel.getCurrentUser().observe(this, Observer {
            if (prefs.isLoggedIn) {
                Snackbar.make(
                    container,
                    String.format(getString(R.string.welcome_text), it?.name ?: it?.email),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Continue Shopping") {
                        intentTo(HomeActivity::class.java, true)
                    }
                    .show()
            }
        })
    }

    fun register(v: View?) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            authService.createUserWithEmailAndPassword(LoginRequest(username.text.toString(), password.text.toString()))
                .observe(this, Observer {
                    when (it) {
                        is Outcome.Success -> {
                            debugLog("Success: ${it.data}")
                            loginUser(it.data)
                        }

                        is Outcome.Failure -> {
                            debugLog("Failure: ${it.e.localizedMessage}")
                            authSnackbar.setText(it.e.localizedMessage).setDuration(BaseTransientBottomBar.LENGTH_LONG)
                                .show()
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.GONE
                            binding.content.visibility = View.VISIBLE
                        }

                        is Outcome.Progress -> {
                            debugLog("Login call in progress")
                            authSnackbar.show()
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.VISIBLE
                            binding.content.visibility = View.GONE
                        }
                    }
                })

        } else toast("Enter your email and password")
    }

    private fun loginUser(user: User) {
        ioScope.launch {
            userViewModel.addUser(user)
            prefs.key = user.key
            delay(2000)

            uiScope.launch {
                showConfirmationToast(user.avatar, user.email)
                intentTo(HomeActivity::class.java, true)
            }
        }
    }

    fun login(view: View) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            authService.loginWithEmailAndPassword(LoginRequest(username.text.toString(), password.text.toString()))
                .observe(this, Observer {
                    when (it) {
                        is Outcome.Success -> {
                            debugLog("Success: ${it.data}")
                            val user = it.data
                            loginUser(user)
                        }

                        is Outcome.Failure -> {
                            authSnackbar.setText(it.e.localizedMessage).setDuration(BaseTransientBottomBar.LENGTH_LONG)
                                .show()
                            debugLog("Failure: ${it.e.localizedMessage}")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.GONE
                            binding.content.visibility = View.VISIBLE
                        }

                        is Outcome.Progress -> {
                            authSnackbar.show()
                            debugLog("Login call in progress")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.VISIBLE
                            binding.content.visibility = View.GONE
                        }
                    }
                })

        } else toast("Enter your email and password")
    }

    private fun googleLogin() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        GoogleSignIn.getClient(this, gso).apply {
            TransitionManager.beginDelayedTransition(binding.container)
            binding.loading.visibility = View.VISIBLE
            binding.content.visibility = View.GONE
            startActivityForResult(this.signInIntent, RC_GOOGLE_LOGIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_LOGIN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    authSnackbar.show()
                    val acctTask = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = acctTask.getResult(ApiException::class.java)
                        updateUI(account)
                    } catch (ex: ApiException) {
                        // Login failed
                        if (authSnackbar.isShown) authSnackbar.dismiss()
                        debugLog(ex.localizedMessage)
                        toast(ex.localizedMessage)

                        TransitionManager.beginDelayedTransition(binding.container)
                        binding.loading.visibility = View.GONE
                        binding.content.visibility = View.VISIBLE
                    }
                }

                else -> {
                    // Login cancelled
                    authSnackbar.setText("Login failed").setDuration(BaseTransientBottomBar.LENGTH_LONG).show()

                    TransitionManager.beginDelayedTransition(binding.container)
                    binding.loading.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        debugLog(account?.id)
        if (account == null) {
            toast("Please sign in again. It seems there\'s a problem with your login", true)
            return
        }
        debugLog("Account Id token: ${account.idToken}")

        if (account.idToken.isNullOrEmpty()) {
            if (authSnackbar.isShown) authSnackbar.dismiss()
            debugLog("Failure: Account id token is null")
            TransitionManager.beginDelayedTransition(binding.container)
            binding.loading.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        } else {
            authService.authenticateCustomer(
                OAuthRequest(
                    account.email,
                    account.idToken!!,
                    account.displayName,
                    account.photoUrl.toString()
                )
            )
                .observe(this, Observer {
                    when (it) {
                        is Outcome.Success -> {
                            debugLog("Success: ${it.data}")
                            val user = it.data
                            loginUser(user)
                        }

                        is Outcome.Failure -> {
                            authSnackbar.setText(it.e.localizedMessage).setDuration(BaseTransientBottomBar.LENGTH_LONG)
                                .show()
                            debugLog("Failure: ${it.e.localizedMessage}")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.GONE
                            binding.content.visibility = View.VISIBLE
                        }

                        is Outcome.Progress -> {
                            authSnackbar.show()
                            debugLog("Login call in progress")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.VISIBLE
                            binding.content.visibility = View.GONE
                        }
                    }
                })
        }
    }


    companion object {
        private const val RC_GOOGLE_LOGIN = 9
    }
}
