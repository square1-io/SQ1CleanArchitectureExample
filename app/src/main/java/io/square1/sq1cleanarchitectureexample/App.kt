package io.square1.sq1cleanarchitectureexample

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.square1.sq1cleanarchitectureexample.di.components.ApplicationComponent
import io.square1.sq1cleanarchitectureexample.di.components.DaggerApplicationComponent
import io.square1.sq1cleanarchitectureexample.di.modules.ApplicationModule

class App : Application() {
    val component: ApplicationComponent
        get() = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration: RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}