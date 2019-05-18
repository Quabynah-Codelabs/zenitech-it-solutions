package io.codelabs.zenitech.core.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductRepository constructor(private val dao: RoomAppDao) {

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
            _product.postValue(dao.getProduct(key))
        }
        return _product
    }

    fun getAllProducts(): MutableList<Product> = mutableListOf<Product>().apply {
        clear()
        addAll(dao.getAllProducts())
    }

}