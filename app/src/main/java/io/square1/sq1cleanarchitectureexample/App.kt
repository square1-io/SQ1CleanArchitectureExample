package io.square1.sq1cleanarchitectureexample

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.square1.remote.repository.NWGetUserRemoteRepositoryImpl
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.sq1cleanarchitectureexample.viewmodel.GetUsersViewModel
import io.square1.storage.repository.RLMGetUserStorageRepositoryImpl
import io.square1.storage.repository.RLMGetUsersStorageRepository
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class App : Application() {
    private var realm: Realm? = null

    override fun onCreate() {
        super.onCreate()
        realm = initRealm()
        startKoin(this, listOf(appModule))
    }

    private fun initRealm(): Realm? {
        Realm.init(this)
        val realmConfiguration: RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        // delete all realm objects
        realm.deleteAll()
        //commit realm changes
        realm.commitTransaction()

        return realm
    }

    val appModule = module {
        realm?.let {
            val realm = it
            factory { GetUsersViewModel(get(), get()) }
            single { RLMGetUserStorageRepositoryImpl(realm) as RLMGetUsersStorageRepository }
            single { NWGetUserRemoteRepositoryImpl() as NWGetUsersRemoteRepository }
        }
    }
}