package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.zenitech.core.datasource.repository.UserRepository

class UserVMFactory constructor(private val repo: UserRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UserViewModel(repo) as T
    }
}