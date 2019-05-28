package io.codelabs.zenitech.core.database

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.core.dbutil.CartRequest
import io.codelabs.zenitech.core.dbutil.CustomerRequest
import io.codelabs.zenitech.data.Cart
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface DatabaseService {

    //region CUSTOMER
    @POST("api/me")
    fun getCurrentCustomer(@Body request: CustomerRequest): RetrofitLiveData<User>

    @POST("api/customers/{id}")
    fun updateCustomer(@Path("id") key: String): RetrofitLiveData<Void>
    //endregion CUSTOMER

    //region PRODUCTS
    @POST("api/products")
    fun loadProducts(): RetrofitLiveData<MutableList<Product>>

    @POST("api/products/{id}")
    fun getProductById(@Path("id") id: String): RetrofitLiveData<Product>
    //endregion PRODUCTS

    //region CART
    @POST("api/cart")
    fun addToCart(@Body request: CartRequest): RetrofitLiveData<Void>

    @POST("api/cart/{id}")
    fun getCustomerCart(@Path("id") key: String): RetrofitLiveData<MutableList<Cart>>

    @POST("api/cart/{user}/{id}")
    fun getCartItem(@Path("user") user: String, @Path("id") key: String): RetrofitLiveData<Cart>

    @DELETE("api/cart/{id}")
    fun deleteCart(@Path("id") key: String): RetrofitLiveData<Void>
    //endregion CART
}