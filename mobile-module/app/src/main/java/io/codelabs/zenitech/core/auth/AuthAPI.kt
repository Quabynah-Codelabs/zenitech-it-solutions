package io.codelabs.zenitech.core.auth

import android.os.Environment
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.codelabs.sdk.util.network.LiveDataCallAdapterFactory
import io.codelabs.zenitech.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object AuthAPI {

    fun getAuthService(): AuthService {

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                ).connectTimeout(60L, TimeUnit.SECONDS)
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