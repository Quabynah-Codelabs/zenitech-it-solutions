package io.codelabs.zenitech.core.auth

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.User
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    companion object {
        const val BASE_URL = "https://us-central1-zenitech-solutions.cloudfunctions.net/"
    }

    @POST("login")
    fun loginWithEmailAndPassword(@Body request: LoginRequest): RetrofitLiveData<User>
}