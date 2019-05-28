package io.codelabs.zenitech.core.datasource.repository

import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.zenitech.core.database.DatabaseAPI
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.core.dbutil.Callback
import io.codelabs.zenitech.core.dbutil.CustomerRequest
import io.codelabs.zenitech.data.Cart
import io.codelabs.zenitech.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserRepository constructor(
    private val dao: RoomAppDao,
    private val prefs: Preferences,
    private val api: DatabaseAPI
) {

    fun getCurrentUser(callback: Callback<User?>) {
        callback.onInit()
        if (prefs.isLoggedIn) {
            GlobalScope.launch(Dispatchers.Main) {
                api.getDatabaseService().getCurrentCustomer(CustomerRequest(prefs.key!!))
                    .observeForever {
                        debugLog("User query result: $it")
                        when (it) {
                            is Outcome.Success -> {
                                callback.onSuccess(it.data)

                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        dao.addUser(it.data)
                                    } catch (e: Exception) {
                                        debugLog(e.localizedMessage)
                                    }
                                    callback.onComplete()
                                }
                            }

                            is Outcome.Failure -> {
                                callback.onError(it.e.localizedMessage)
                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        callback.onSuccess(dao.getUser(prefs.key!!))
                                    } catch (e: Exception) {
                                        debugLog(e.localizedMessage)
                                    }
                                    callback.onComplete()
                                }
                            }
                        }
                    }
            }
        }
    }

    fun getCart(callback: Callback<MutableList<Cart>>) {
        callback.onInit()
        if (prefs.isLoggedIn) {
            GlobalScope.launch(Dispatchers.Main) {
                api.getDatabaseService().getCustomerCart(prefs.key.toString()).observeForever {
                    when (it) {
                        is Outcome.Success -> {
                            callback.onSuccess(it.data)
                            callback.onComplete()
                        }

                        is Outcome.Failure -> {
                            callback.onError(it.e.localizedMessage)
                            callback.onComplete()
                        }
                    }
                }

            }
        }
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