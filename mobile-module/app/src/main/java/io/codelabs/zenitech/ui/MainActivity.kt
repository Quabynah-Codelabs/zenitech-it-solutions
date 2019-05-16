package io.codelabs.zenitech.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.sdk.util.toast
import io.codelabs.zenitech.BuildConfig
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.auth.LoginRequest
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.core.util.isNotEmpty
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navHome(v: View?) = intentTo(HomeActivity::class.java)

    fun login(view: View) {

        if (username.isNotEmpty() && password.isNotEmpty()) {
            authService.loginWithEmailAndPassword(LoginRequest(username.text.toString(), password.text.toString()))
                .observe(this, Observer {
                    when (it) {
                        is Outcome.Success -> {
                            debugLog("Success: ${it.data}")
                        }

                        is Outcome.Failure -> {
                            debugLog("Failure: ${it.e.localizedMessage}")
                        }

                        is Outcome.Progress -> {
                            debugLog("Login Call in progress")
                        }
                    }
                })

        } else toast("Enter your email and password")
    }

    fun googleLogin(view: View) {

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_AUTH_CLIENT_ID)
            .requestEmail()
            .requestId()
            .build()

        GoogleSignIn.getClient(this, gso).apply {
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
                    }
                }

                else -> {
                    // Login cancelled
                    toast("Login failed")
                }
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        debugLog(account?.id)
    }

    companion object {
        private const val RC_GOOGLE_LOGIN = 9
    }
}
