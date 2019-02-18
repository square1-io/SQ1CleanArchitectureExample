package io.square1.remote.repository

import io.reactivex.Observable
import io.square1.remote.ApiServiceInterface
import io.square1.remote.model.Results

class NWGetUserRemoteRepositoryImpl : NWGetUsersRemoteRepository {

    private val apiServiceInterface by lazy {
        ApiServiceInterface.create()
    }

    override fun getUsers(): Observable<Results> {
        return apiServiceInterface.getUsers()
    }
}