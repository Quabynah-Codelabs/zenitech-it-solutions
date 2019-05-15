package io.codelabs.zenitech.core.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductRepository constructor(private val dao: RoomAppDao) {

    fun addProduct(vararg product: Product) = GlobalScope.launch {
        dao.addProduct(*product)
    }

    fun removeProduct(vararg product: Product) = GlobalScope.launch {
        dao.removeProduct(*product)
    }

    fun getProductById(key: String): LiveData<Product> {
        val _product = MutableLiveData<Product>()
        GlobalScope.launch {
            _product.postValue(dao.getProduct(key))
        }
        return _product
    }

    suspend fun getAllProducts(): LiveData<MutableList<Product>> {
        val _products = MutableLiveData<MutableList<Product>>()

        withContext(Dispatchers.IO) {
            _products.postValue(dao.getAllProducts())
        }

        return _products
    }

}