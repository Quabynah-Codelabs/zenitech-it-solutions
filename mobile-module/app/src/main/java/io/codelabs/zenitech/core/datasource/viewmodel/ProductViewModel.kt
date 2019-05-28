package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.dbutil.Callback
import io.codelabs.zenitech.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel constructor(private val repository: ProductRepository) : ViewModel() {

    private val _liveProducts: MutableLiveData<MutableList<Product>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllProducts(object : Callback<MutableList<Product>> {
                override fun onError(error: String?) {
                    debugLog(error)
                }

                override fun onSuccess(result: MutableList<Product>) {
                    _liveProducts.postValue(result)
                }
            })
        }
    }

    val liveProducts: LiveData<MutableList<Product>> = _liveProducts
}