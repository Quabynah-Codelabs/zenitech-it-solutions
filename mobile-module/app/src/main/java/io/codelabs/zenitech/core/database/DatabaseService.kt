package io.codelabs.zenitech.core.database

import io.codelabs.sdk.util.network.RetrofitLiveData
import io.codelabs.zenitech.core.dbutil.CartRequest
import io.codelabs.zenitech.core.dbutil.CustomerRequest
import io.codelabs.zenitech.data.Cart
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface DatabaseService {

    //region CUSTOMER
    @POST("api/me")
    fun getCurrentCustomer(@Body request: CustomerRequest): RetrofitLiveData<User>

    @POST("api/customers/{id}")
    fun updateCustomer(@Path("id") key: String): Deferred<Void>
    //endregion CUSTOMER

    //region PRODUCTS
    @POST("api/products")
    fun loadProducts(): RetrofitLiveData<MutableList<Product>>

    @POST("api/products/{id}")
    fun getProductByIdAsync(@Path("id") id: String): Deferred<Product>

    @PUT("api/products/{id}")
    fun updateProductAsync(@Body product: Product) : Deferred<Void>
    //endregion PRODUCTS

    //region CART
    @POST("api/cart")
    fun addToCart(@Body request: CartRequest): Deferred<Void>

    @POST("api/cart/{id}")
    fun getCustomerCart(@Path("id") key: String): RetrofitLiveData<MutableList<Cart>>

    @POST("api/cart/{user}/{id}")
    fun getCartItem(@Path("user") user: String, @Path("id") key: String): Deferred<Cart>

    @DELETE("api/cart/{id}")
    fun deleteCart(@Path("id") key: String): Deferred<Void>
    //endregion CART
}