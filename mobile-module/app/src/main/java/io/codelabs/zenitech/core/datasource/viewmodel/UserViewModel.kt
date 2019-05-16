package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.codelabs.zenitech.core.datasource.repository.UserRepository
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel constructor(private val repository: UserRepository) : ViewModel() {

    private var _liveUser: MutableLiveData<User> = MutableLiveData()

    fun getCurrentUser(): LiveData<User> = _liveUser

    fun updateUser(user: User) = repository.updateUser(user)

    fun addUser(user: User) = repository.addUser(user)

    fun removeUser(user: User) = repository.removeUser(user)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _liveUser.postValue(repository.getCurrentUser())
        }
    }

}