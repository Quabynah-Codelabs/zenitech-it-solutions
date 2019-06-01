package io.codelabs.zenitech.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

                val v = layoutInflater.inflate(R.layout.dialog_password_reset, null, false)
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.reset_password))
                    .setNegativeButton("Dismiss") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Send link") { dialog, _ ->
                        val username = v.findViewById<EditText>(R.id.username).text.toString()
                        if (username.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                            dialog.dismiss()
                            toast("Please check your email for the reset link")
                            authService.resetPassword(LoginRequest(username, "")).observe(this, Observer {
                                when (it) {
                                    is Outcome.Success -> {
                                        toast("Please sign in again")
                                    }

                                    is Outcome.Progress -> {
                                        debugLog("Password reset in progress...")
                                    }

                                    else -> {
                                        toast("Unable to send email to this address. Please try again with a valid email address")
                                    }
                                }
                            })
                        } else toast("Please enter a valid email address")

                    }
                    .create().apply {
                        setView(v)
                        setCanceledOnTouchOutside(false)
                        show()
                    }
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
            ioScope.launch {
                loginUser(
                    authService.createUserWithEmailAndPasswordAsync(
                        LoginRequest(
                            username.text.toString(),
                            password.text.toString()
                        )
                    ).await()
                )
            }

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
            ioScope.launch {
                loginUser(
                    authService.loginWithEmailAndPasswordAsync(
                        LoginRequest(
                            username.text.toString(),
                            password.text.toString()
                        )
                    ).await()
                )
            }

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

        if (account.idToken.isNullOrEmpty()) {
            if (authSnackbar.isShown) authSnackbar.dismiss()
            debugLog("Failure: Account id token is null")
            TransitionManager.beginDelayedTransition(binding.container)
            binding.loading.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        } else {
            ioScope.launch {
                loginUser(
                    authService.authenticateCustomerAsync(
                        OAuthRequest(
                            account.email,
                            account.idToken!!,
                            account.displayName,
                            account.photoUrl.toString()
                        )
                    ).await()
                )
            }
        }
    }


    companion object {
        private const val RC_GOOGLE_LOGIN = 9
    }
}
