package io.codelabs.zenitech.core.datasource.repository

import io.codelabs.sdk.util.debugLog
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
            try {
                if (prefs.isLoggedIn) user = dao.getUser(prefs.key!!)
            } catch (e: Exception) {
                debugLog(e.localizedMessage)
            }
        }
        return user
    }

    fun updateUser(user: User) = GlobalScope.launch(Dispatchers.IO) {
        dao.updateUser(user)
    }

    fun getAllUsers(): MutableList<User> = mutableListOf<User>().apply {
        GlobalScope.launch(Dispatchers.IO) {
            addAll(dao.getAllUsers())
        }
    }

    fun addUser(user: User) = GlobalScope.launch(Dispatchers.IO) {
        dao.addUser(user)
    }

    fun removeUser(user: User) = GlobalScope.launch(Dispatchers.IO) {
        dao.deleteUser(user)
    }

}