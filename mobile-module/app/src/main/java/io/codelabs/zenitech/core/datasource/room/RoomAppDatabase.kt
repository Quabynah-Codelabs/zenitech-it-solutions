package io.codelabs.zenitech.core.datasource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codelabs.zenitech.BuildConfig
import io.codelabs.zenitech.core.DATABASE_NAME
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.User

@Database(entities = [User::class, Product::class], version = BuildConfig.VERSION_CODE, exportSchema = true)
abstract class RoomAppDatabase : RoomDatabase() {

    abstract fun dao(): RoomAppDao

    companion object {
        @Volatile
        private var instance: RoomAppDatabase? = null

        fun get(context: Context): RoomAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, RoomAppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }

}