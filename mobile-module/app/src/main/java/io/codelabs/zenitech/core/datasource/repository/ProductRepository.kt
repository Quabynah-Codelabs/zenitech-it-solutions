package io.codelabs.zenitech.core.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductRepository constructor(private val dao: RoomAppDao) {

    fun addProduct(vararg product: Product) = GlobalScope.launch(Dispatchers.IO) {
        dao.addProduct(*product)
    }

    fun removeProduct(product: Product) = GlobalScope.launch(Dispatchers.IO) {
        dao.removeProduct(product)
    }

    fun getProductById(key: String): LiveData<Product> {
        val _product = MutableLiveData<Product>()
        GlobalScope.launch(Dispatchers.IO) {
            _product.postValue(dao.getProduct(key))
        }
        return _product
    }

    fun getAllProducts(): LiveData<MutableList<Product>> {
        val _products = MutableLiveData<MutableList<Product>>()

        GlobalScope.launch(Dispatchers.IO) {
            debugLog("Loading products for cart")
            _products.postValue(dao.getAllProducts())
        }

        return _products
    }

}