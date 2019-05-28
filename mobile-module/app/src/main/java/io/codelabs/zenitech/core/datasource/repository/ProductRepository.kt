package io.codelabs.zenitech.core.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.zenitech.core.database.DatabaseAPI
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.core.dbutil.Callback
import io.codelabs.zenitech.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductRepository constructor(private val dao: RoomAppDao, private val api: DatabaseAPI) {

    fun addProduct(vararg product: Product) = GlobalScope.launch(Dispatchers.IO) {
        dao.addProduct(*product)
    }

    fun addProduct(products: MutableList<Product>) = GlobalScope.launch(Dispatchers.IO) {
        dao.removeProductList(dao.getAllProducts())
        dao.addProductList(products)
    }

    fun addFavorite(product: Product) = GlobalScope.launch(Dispatchers.IO) {
        dao.updateProduct(product.apply {
            isWishListItem = true
        })
    }

    fun removeProduct(product: Product) = GlobalScope.launch(Dispatchers.IO) {
        dao.removeProduct(product)
    }

    fun removeFavorite(product: Product) = GlobalScope.launch(Dispatchers.IO) {
        dao.updateProduct(product.apply {
            isWishListItem = false
        })
    }

    fun getProductById(key: String): LiveData<Product> {
        val _product = MutableLiveData<Product>()
        GlobalScope.launch(Dispatchers.IO) {
            val product = api.getDatabaseService().getProductByIdAsync(key).await()
            _product.postValue(product)
        }
        return _product
    }

    fun getAllProducts(callback: Callback<MutableList<Product>>) {
        callback.onInit()
        api.getDatabaseService().loadProducts().observeForever {
            when (it) {
                is Outcome.Success -> {
                    debugLog("Products found: ${it.data.size}")
                    callback.onSuccess(it.data)

                    GlobalScope.launch(Dispatchers.IO) {
                        for (product in it.data) {
                            dao.addProduct(product)
                        }
                        callback.onComplete()
                    }
                }

                is Outcome.Progress -> { /*DO Nothing*/ }

                else -> {
                    callback.onError("Unable to fetch products")
                    callback.onComplete()
                }
            }
        }
    }

}