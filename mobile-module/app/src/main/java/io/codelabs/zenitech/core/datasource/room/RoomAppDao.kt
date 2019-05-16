package io.codelabs.zenitech.core.datasource.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.codelabs.zenitech.data.Issue
import io.codelabs.zenitech.data.Product
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(vararg product: Product)

    @Delete
    fun removeProduct(vararg product: Product)

    @Query("SELECT * FROM products WHERE `key` = :key")
    fun getProduct(key: String): Product

    @Query("SELECT * FROM products ORDER BY uploadTime DESC")
    fun getAllProducts(): MutableList<Product>

    @Query("SELECT * FROM products ORDER BY uploadTime DESC")
    /*suspend*/ fun getLiveProducts(): MutableList<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addIssue(vararg issue: Issue)

    @Delete
    fun removeIssue(vararg issue: Issue)

    @Query("SELECT * FROM issues WHERE `key` = :key")
    fun getIssue(key: String): Issue

    @Query("SELECT * FROM issues ORDER BY timestamp DESC")
    fun getAllIssues(): MutableList<Issue>

    @Query("SELECT * FROM issues ORDER BY timestamp DESC")
    /*suspend*/ fun getLiveIssues(): MutableList<Issue>
}