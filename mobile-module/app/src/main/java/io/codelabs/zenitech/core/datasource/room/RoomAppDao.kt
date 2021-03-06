package io.codelabs.zenitech.core.datasource.room

import androidx.room.*
import io.codelabs.zenitech.data.Issue
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.User

@Dao
interface RoomAppDao {

    @Query("SELECT * FROM users WHERE `key` = :key")
    fun getUser(key: String): User


    @Query("SELECT * FROM users ORDER BY `key`")
    fun getAllUsers(): MutableList<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(vararg product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProductList(products: MutableList<Product>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(product: Product)

    @Delete
    fun removeProduct(vararg product: Product)

    @Delete
    fun removeProductList(products: MutableList<Product>)

    @Query("SELECT * FROM products WHERE `key` = :key")
    fun getProduct(key: String): Product

    @Query("SELECT * FROM products ORDER BY uploadTime DESC")
    fun getAllProducts(): MutableList<Product>

    @Query("SELECT * FROM products WHERE isWishListItem = :state ORDER BY uploadTime DESC")
    suspend fun getAllFavoriteItems(state: Boolean = true): MutableList<Product>

    @Query("SELECT * FROM products ORDER BY uploadTime DESC")
    suspend fun getLiveProducts(): MutableList<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addIssue(vararg issue: Issue)

    @Delete
    fun removeIssue(vararg issue: Issue)

    @Query("SELECT * FROM issues WHERE `key` = :key")
    fun getIssue(key: String): Issue

    @Query("SELECT * FROM issues ORDER BY timestamp DESC")
    fun getAllIssues(): MutableList<Issue>

    @Query("SELECT * FROM issues ORDER BY timestamp DESC")
    suspend fun getLiveIssues(): MutableList<Issue>
}