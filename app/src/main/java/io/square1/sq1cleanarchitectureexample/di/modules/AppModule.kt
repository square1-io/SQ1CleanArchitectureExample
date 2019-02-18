package io.square1.sq1cleanarchitectureexample.di.modules

import io.realm.Realm
import io.square1.remote.repository.NWGetUserRemoteRepositoryImpl
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.sq1cleanarchitectureexample.viewmodel.GetUsersViewModel
import io.square1.storage.repository.RLMGetUserStorageRepositoryImpl
import io.square1.storage.repository.RLMGetUsersStorageRepository
import org.koin.dsl.module.module


open class AppModule(realm: Realm) {
    val appModule = module {
        factory { GetUsersViewModel(get(), get()) }
        single { RLMGetUserStorageRepositoryImpl(realm) as RLMGetUsersStorageRepository }
        single { NWGetUserRemoteRepositoryImpl() as NWGetUsersRemoteRepository }
    }
}