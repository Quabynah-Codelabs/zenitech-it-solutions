package io.codelabs.zenitech.core.auth

import android.os.Environment
import io.codelabs.sdk.util.network.LiveDataCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object AuthAPI {

    fun getAuthService(): AuthService {
        val retrofit = Retrofit.Builder()
            .baseUrl(AuthService.BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .cache(
                        Cache(
                            File("${Environment.getExternalStorageDirectory().parent}/zenitech-cache"),
                            1024.times(1024).times(10)
                        )
                    )
                    .build()
            ).build()

        return retrofit.create(AuthService::class.java)
    }

}