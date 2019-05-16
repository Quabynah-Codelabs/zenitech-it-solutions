package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.sdk.util.toast
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

                        }
                    }
                })
        } else toast("Enter your email and password")
    }
}
