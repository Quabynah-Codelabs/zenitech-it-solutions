package io.codelabs.zenitech.core.database

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.Product
import retrofit2.http.POST
import retrofit2.http.Path

interface DatabaseService {

    @POST("/products")
    fun loadProducts(): RetrofitLiveData<MutableList<Product>>

    @POST("/products/{id}")
    fun getProductById(@Path("id") id: String): RetrofitLiveData<Product>
}