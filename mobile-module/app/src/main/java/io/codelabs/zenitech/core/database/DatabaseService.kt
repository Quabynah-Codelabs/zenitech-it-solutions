package io.codelabs.zenitech.core.database

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DatabaseService {

    data class CustomerRequest(val key: String)

    @POST("/me")
    fun getCurrentCustomer(@Body request: CustomerRequest): RetrofitLiveData<User>

    @POST("/customers/{id}")
    fun updateCustomer(@Path("id") key: String): RetrofitLiveData<Void>

    @POST("/products")
    fun loadProducts(): RetrofitLiveData<MutableList<Product>>

    @POST("/products/{id}")
    fun getProductById(@Path("id") id: String): RetrofitLiveData<Product>
}