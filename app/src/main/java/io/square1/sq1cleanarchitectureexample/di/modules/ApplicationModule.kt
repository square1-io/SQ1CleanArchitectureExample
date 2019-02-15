package io.square1.sq1cleanarchitectureexample.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.square1.remote.repository.NWGetUserRemoteRepositoryImpl
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.storage.repository.RLMGetUserStorageRepositoryImpl
import io.square1.storage.repository.RLMGetUsersStorageRepository
import javax.inject.Singleton

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
class ApplicationModule(private val androidApplication: Application) {

    @Provides
    @Singleton
    fun application(): Application {
        return androidApplication
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return androidApplication
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(nwGetUserRemoteRepositoryImpl: NWGetUserRemoteRepositoryImpl): NWGetUsersRemoteRepository {
        return nwGetUserRemoteRepositoryImpl

    }

    @Provides
    @Singleton
    fun provideStorageRepository(rlmGetUsersStorageRepository: RLMGetUserStorageRepositoryImpl): RLMGetUsersStorageRepository {
        return rlmGetUsersStorageRepository

    }
}