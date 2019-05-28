package io.codelabs.zenitech.core.auth

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.core.dbutil.LoginRequest
import io.codelabs.zenitech.core.dbutil.OAuthRequest
import io.codelabs.zenitech.data.User
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/login")
    fun loginWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>

    @POST("api/register")
    fun createUserWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>

    @POST("api/oauth")
    fun authenticateCustomer(
        @Body request: OAuthRequest
    ): RetrofitLiveData<User>
}