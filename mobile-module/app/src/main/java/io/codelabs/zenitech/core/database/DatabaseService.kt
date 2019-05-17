package io.codelabs.zenitech.core.database

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.Product
import retrofit2.http.GET

interface DatabaseService {

    @GET("/products")
    fun loadProducts(): RetrofitLiveData<MutableList<Product>>
}