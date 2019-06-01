package io.codelabs.zenitech.core.auth

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.core.dbutil.LoginRequest
import io.codelabs.zenitech.core.dbutil.OAuthRequest
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/login")
    fun loginWithEmailAndPasswordAsync(@Body request: LoginRequest): Deferred<User>

    @POST("api/register")
    fun createUserWithEmailAndPasswordAsync(@Body request: LoginRequest): Deferred<User>

    @POST("api/oauth")
    fun authenticateCustomerAsync(@Body request: OAuthRequest): Deferred<User>

    @POST("api/reset-password")
    fun resetPassword(@Body request: LoginRequest): RetrofitLiveData<Void>
}