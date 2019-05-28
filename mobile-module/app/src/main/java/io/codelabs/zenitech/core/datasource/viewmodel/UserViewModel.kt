package io.codelabs.zenitech.core.datasource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.core.datasource.repository.UserRepository
import io.codelabs.zenitech.core.dbutil.Callback
import io.codelabs.zenitech.data.Cart
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel constructor(private val repository: UserRepository) : ViewModel() {

    private var _liveUser: MutableLiveData<User> = MutableLiveData()
    var liveCart: MutableLiveData<MutableList<Cart>> = MutableLiveData()

    fun getCurrentUser(): LiveData<User> = _liveUser

    fun updateUser(user: User) = repository.updateUser(user)

    fun addUser(user: User) = repository.addUser(user)

    fun removeUser(user: User) = repository.removeUser(user)

    fun getAll() = repository.getAllUsers()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentUser(object : Callback<User?> {
                override fun onError(error: String?) {
                    debugLog(error)
                }

                override fun onSuccess(result: User?) {
                    _liveUser.postValue(result)
                }
            })

            repository.getCart(object: Callback<MutableList<Cart>> {
                override fun onError(error: String?) {

                }

                override fun onSuccess(result: MutableList<Cart>) {
                    liveCart.postValue(result)
                }
            })
        }
    }

}