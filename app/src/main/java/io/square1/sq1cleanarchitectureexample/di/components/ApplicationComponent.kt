package io.square1.sq1cleanarchitectureexample.di.components

import android.app.Application
import dagger.Component
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.sq1cleanarchitectureexample.di.modules.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(androidApplication: Application)

    fun repository(): NWGetUsersRemoteRepository
}