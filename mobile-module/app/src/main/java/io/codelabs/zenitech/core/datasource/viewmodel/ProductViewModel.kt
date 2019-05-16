package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.data.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductViewModel constructor(private val repository: ProductRepository) : ViewModel() {

    private val _liveProducts = MutableLiveData<MutableList<Product>>()

    val liveProducts: LiveData<MutableList<Product>> = _liveProducts

    init {
        GlobalScope.launch {
            _liveProducts.postValue(repository.getAllProducts())
        }
    }
}