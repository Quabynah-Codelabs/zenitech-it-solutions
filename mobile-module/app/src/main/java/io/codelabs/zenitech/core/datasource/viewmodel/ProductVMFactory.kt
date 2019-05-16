package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.zenitech.core.datasource.repository.ProductRepository

class ProductVMFactory constructor(private val repository: ProductRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProductViewModel(repository) as T
    }
}