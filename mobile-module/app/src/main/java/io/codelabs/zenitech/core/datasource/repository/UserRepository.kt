package io.codelabs.zenitech.core.datasource.repository

import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository constructor(
    private val dao: RoomAppDao,
    private val prefs: Preferences/*todo: add remote datasource here*/
) {

    suspend fun getCurrentUser(): User? {
        var user: User? = null
        withContext(Dispatchers.IO) {
            user = dao.getUser(prefs.key!!)
        }
        return user
    }

    fun updateUser(user: User) = GlobalScope.launch {
        dao.updateUser(user)
    }

    fun addUser(user: User) = GlobalScope.launch {
        dao.addUser(user)
    }

    fun removeUser(user: User) = GlobalScope.launch {
        dao.deleteUser(user)
    }

}