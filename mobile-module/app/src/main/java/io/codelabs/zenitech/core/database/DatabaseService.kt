package io.codelabs.zenitech.core.database

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.data.Product
import retrofit2.http.POST

interface DatabaseService {

    @POST("/products")
    fun loadProducts(): RetrofitLiveData<MutableList<Product>>
}