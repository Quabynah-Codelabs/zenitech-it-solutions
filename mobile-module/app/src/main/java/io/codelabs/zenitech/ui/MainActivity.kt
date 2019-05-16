package io.codelabs.zenitech.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.sdk.util.showConfirmationToast
import io.codelabs.sdk.util.toast
import io.codelabs.zenitech.BuildConfig
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.auth.LoginRequest
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.core.util.isNotEmpty
import io.codelabs.zenitech.data.BaseDataModel
import io.codelabs.zenitech.data.User
import io.codelabs.zenitech.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.loading.visibility = View.GONE
    }

    override fun onEnterAnimationComplete() = if (prefs.isLoggedIn) showHomePrompt() else showGoogleLoginPrompt()

    private fun showGoogleLoginPrompt() {
        Snackbar.make(container, getString(R.string.login_google), Snackbar.LENGTH_INDEFINITE)
            .setAction("Sign In") {
                googleLogin()
            }
            .show()
    }

    private fun showHomePrompt() {

        TransitionManager.beginDelayedTransition(binding.container)
        binding.loading.visibility = View.GONE
        binding.content.visibility = View.GONE
        Snackbar.make(container, getString(R.string.welcome_text), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continue Shopping") {
                intentTo(HomeActivity::class.java, true)
            }
            .show()
    }

    fun register(v: View?) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            authService.createUserWithEmailAndPassword(LoginRequest(username.text.toString(), password.text.toString()))
                .observe(this, Observer {
                    when (it) {
                        is Outcome.Success -> {
                            debugLog("Success: ${it.data}")
                            val user = it.data
                            userViewModel.addUser(user)
                            prefs.key = user.key
                            showConfirmationToast(user.avatar, user.email)
                            intentTo(HomeActivity::class.java, true)
                        }

                        is Outcome.Failure -> {
                            debugLog("Failure: ${it.e.localizedMessage}")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.GONE
                            binding.content.visibility = View.VISIBLE
                        }

                        is Outcome.Progress -> {
                            debugLog("Login call in progress")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.VISIBLE
                            binding.content.visibility = View.GONE
                        }
                    }
                })

        } else toast("Enter your email and password")
    }

    fun login(view: View) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            authService.loginWithEmailAndPassword(LoginRequest(username.text.toString(), password.text.toString()))
                .observe(this, Observer {
                    when (it) {
                        is Outcome.Success -> {
                            debugLog("Success: ${it.data}")
                            val user = it.data
                            userViewModel.addUser(user)
                            prefs.key = user.key
                            showConfirmationToast(user.avatar, user.email)
                            intentTo(HomeActivity::class.java, true)
                        }

                        is Outcome.Failure -> {
                            debugLog("Failure: ${it.e.localizedMessage}")
                            TransitionManager.beginDelayedTransition(binding.container)
                            binding.loading.visibility = View.GONE
                            binding.content.visibility = View.VISIBLE
                        }

                        is Outcome.Progress -> {
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
            .requestIdToken(BuildConfig.GOOGLE_AUTH_CLIENT_ID)
            .requestEmail()
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
                    val acctTask = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = acctTask.getResult(ApiException::class.java)
                        updateUI(account)
                    } catch (ex: ApiException) {
                        // Login failed
                        debugLog(ex.localizedMessage)
                        toast(ex.localizedMessage)
                        showGoogleLoginPrompt()

                        TransitionManager.beginDelayedTransition(binding.container)
                        binding.loading.visibility = View.GONE
                        binding.content.visibility = View.VISIBLE
                    }
                }

                else -> {
                    // Login cancelled
                    toast("Login failed")
                    showGoogleLoginPrompt()


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

        try {
            val user = User(
                account.id!!, account.displayName ?: "No Name", account.email!!,
                account.photoUrl.toString(), BaseDataModel.ModelType.USER
            )
            userViewModel.addUser(user)
            prefs.key = user.key
            showConfirmationToast(user.avatar, user.email)
            intentTo(HomeActivity::class.java, true)
        } catch (e: Exception) {
            toast("Unable to sign you in...", true)
        }
    }


    companion object {
        private const val RC_GOOGLE_LOGIN = 9
    }
}
