package io.codelabs.zenitech.core

import io.codelabs.zenitech.core.auth.AuthAPI
import io.codelabs.zenitech.core.database.DatabaseAPI
import io.codelabs.zenitech.core.datasource.repository.IssueRepository
import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.datasource.repository.UserRepository
import io.codelabs.zenitech.core.datasource.room.RoomAppDatabase
import io.codelabs.zenitech.core.datasource.viewmodel.ProductVMFactory
import io.codelabs.zenitech.core.datasource.viewmodel.ProductViewModel
import io.codelabs.zenitech.core.datasource.viewmodel.UserVMFactory
import io.codelabs.zenitech.core.datasource.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomModule = module {

    single { RoomAppDatabase.get(androidContext()).dao() }

    factory { UserVMFactory(get()) }

    factory { ProductVMFactory(get()) }

    single { UserRepository(get(), get()) }

    single { ProductRepository(get()) }

    single { IssueRepository(get()) }

    viewModel(PRODUCT_VM) { ProductViewModel(get()) }

    viewModel(USER_VM) { UserViewModel(get()) }
}

val prefsModule = module {
    single { Preferences.getInstance(androidContext()) }
}

val remoteService = module {
    single { AuthAPI.getAuthService() }

    single { DatabaseAPI.get() }
}

val firebaseModule = module {
//    single { Firebase.auth }
//    single { Firebase.firestore }
//    single { Firebase.storage }
}