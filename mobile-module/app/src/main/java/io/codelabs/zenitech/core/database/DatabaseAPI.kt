package io.codelabs.zenitech.core.database

import android.os.Environment
import android.text.format.DateUtils
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.network.LiveDataCallAdapterFactory
import io.codelabs.zenitech.BuildConfig
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.core.util.markAsSynced
import io.codelabs.zenitech.data.Issue
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.SyncableDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class DatabaseAPI {

    companion object {
        @Volatile
        private var instance: DatabaseAPI? = null

        fun get(): DatabaseAPI {
            return instance ?: synchronized(this) {
                instance ?: DatabaseAPI().also { instance = it }
            }
        }
    }

    fun getDatabaseService(): DatabaseService {

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

        return retrofit.create(DatabaseService::class.java)
    }

    suspend fun syncDataWithServer(dao: RoomAppDao, vararg models: MutableList<out SyncableDataModel>) {
        val timeStarted = System.currentTimeMillis()
        debugLog("Data synced started at $timeStarted")
        withContext(Dispatchers.IO) {
            models.forEach { _models ->
                _models.markAsSynced()
                _models.forEach {
                    when (it) {
                        is Product -> {
                            getDatabaseService().updateProductAsync(it).await()
                            dao.addProduct(it)
                        }
                        is Issue -> dao.addIssue(it)
                    }
                }
            }
        }

        debugLog(
            "Data synced finished within ${DateUtils.getRelativeTimeSpanString(
                timeStarted,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )}"
        )
    }

}