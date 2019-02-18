package io.square1.sq1cleanarchitectureexample

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.square1.sq1cleanarchitectureexample.di.modules.AppModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    private var realm: Realm? = null

    override fun onCreate() {
        super.onCreate()
        realm = initRealm()
        realm?.let { startKoin(this, listOf(AppModule(it).appModule)) }
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
}