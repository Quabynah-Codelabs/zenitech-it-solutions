package io.codelabs.zenitech.core.datasource.repository

import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.zenitech.core.database.CustomerRequest
import io.codelabs.zenitech.core.database.DatabaseAPI
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserRepository constructor(
    private val dao: RoomAppDao,
    private val prefs: Preferences,
    private val api: DatabaseAPI
) {

    fun getCurrentUser(): User? {
        var user: User? = null
        if (prefs.isLoggedIn) {
            GlobalScope.launch(Dispatchers.Main) {
                api.getDatabaseService().getCurrentCustomer(CustomerRequest(prefs.key!!))
                    .observeForever {
                        debugLog("User query result: $it")
                        when (it) {
                            is Outcome.Success -> {
                                user = it.data

                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        dao.updateUser(it.data)
                                    } catch (e: Exception) {
                                        debugLog(e.localizedMessage)
                                    }
                                }
                            }

                            is Outcome.Failure -> {
                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        user = dao.getUser(prefs.key!!)
                                    } catch (e: Exception) {
                                        debugLog(e.localizedMessage)
                                    }
                                }
                            }
                        }
                    }
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