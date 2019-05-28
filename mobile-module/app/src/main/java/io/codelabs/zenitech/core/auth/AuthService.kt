package io.codelabs.zenitech.core.auth

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.User
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @POST("/login")
    fun loginWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>

    @POST("/register")
    fun createUserWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>

    @POST("/oauth")
    @FormUrlEncoded
    fun authenticateCustomer(
        @Body request: OAuthRequest
    ): RetrofitLiveData<User>
}