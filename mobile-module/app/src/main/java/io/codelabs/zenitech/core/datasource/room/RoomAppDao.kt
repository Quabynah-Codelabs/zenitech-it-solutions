package io.codelabs.zenitech.core.datasource.room

import androidx.room.*
import io.codelabs.zenitech.data.User

@Dao
interface RoomAppDao {

    @Query("SELECT * FROM users WHERE `key` = :key")
    fun getUser(key: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)
}