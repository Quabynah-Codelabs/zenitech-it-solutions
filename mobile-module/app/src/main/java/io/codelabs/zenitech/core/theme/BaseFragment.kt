package io.codelabs.zenitech.core.theme

import io.codelabs.sdk.view.BaseFragment
import io.codelabs.zenitech.core.database.DatabaseAPI
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import org.koin.android.ext.android.inject

abstract class BaseFragment : BaseFragment() {
    val dao: RoomAppDao by inject()
    val api: DatabaseAPI by inject()
}