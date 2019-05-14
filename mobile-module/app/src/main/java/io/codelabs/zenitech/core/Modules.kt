package io.codelabs.zenitech.core

import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.core.datasource.repository.UserRepository
import io.codelabs.zenitech.core.datasource.room.RoomAppDatabase
import io.codelabs.zenitech.core.datasource.viewmodel.UserVMFactory
import io.codelabs.zenitech.core.datasource.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomModule = module {

    single { RoomAppDatabase.get(androidContext()).dao() }

    factory { UserVMFactory(get()) }

    single { UserRepository(get(), get()) }

    viewModel(USER_VM) { UserViewModel(get()) }
}

val prefsModule = module {
    single { Preferences.getInstance(androidContext()) }
}