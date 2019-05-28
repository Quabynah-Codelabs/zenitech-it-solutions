package io.codelabs.zenitech.core.auth

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.User
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthService {

    @POST("/login")
    fun loginWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>

    @POST("/register")
    fun createUserWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>

    @POST("/oauth")
    fun authenticateCustomer(
        @Field("email") email: String?, @Field("accountId") accountId: String, @Field("username") username: String?, @Field(
            "avatar"
        ) avatar: String?
    ): RetrofitLiveData<User>
}