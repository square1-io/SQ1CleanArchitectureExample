package io.square1.remote.repository

import io.reactivex.Flowable
import io.reactivex.Observable
import io.square1.remote.ApiServiceInterface
import io.square1.remote.model.Results
import javax.inject.Inject

class NWGetUserRemoteRepositoryImpl @Inject constructor() : NWGetUsersRemoteRepository {

    private val apiServiceInterface by lazy {
        ApiServiceInterface.create()
    }

    override fun getUsers(): Observable<Results> {
        return apiServiceInterface.getUsers()
    }
}