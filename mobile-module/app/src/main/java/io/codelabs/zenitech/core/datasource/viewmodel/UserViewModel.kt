package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.codelabs.zenitech.core.datasource.repository.UserRepository
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewModel constructor(private val repository: UserRepository) : ViewModel() {

    private val _liveUser: MutableLiveData<User> = MutableLiveData()

    suspend fun getCurrentUser(): LiveData<User> {
        _liveUser.postValue(repository.getCurrentUser().value)
        return _liveUser
    }

    fun updateUser(user: User) = repository.updateUser(user)

    fun addUser(user: User) = repository.addUser(user)

    fun removeUser(user: User) = repository.removeUser(user)

    init {
        GlobalScope.launch {
            _liveUser.postValue(getCurrentUser().value)
        }
    }

}