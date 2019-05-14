package io.codelabs.zenitech.core.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository constructor(private val dao: RoomAppDao, private val prefs: Preferences) {

    suspend fun getCurrentUser(): LiveData<User> {
        val liveUser: MutableLiveData<User> = MutableLiveData()
        withContext(Dispatchers.IO) {
            if (prefs.isLoggedIn) liveUser.postValue(dao.getUser(prefs.key!!))
        }
        return liveUser
    }

    fun updateUser(user: User) {
        GlobalScope.launch {
            dao.updateUser(user)
        }
    }

    fun addUser(user: User) {
        GlobalScope.launch {
            dao.addUser(user)
        }
    }

    fun removeUser(user: User) {
        GlobalScope.launch {
            dao.deleteUser(user)
        }
    }

}