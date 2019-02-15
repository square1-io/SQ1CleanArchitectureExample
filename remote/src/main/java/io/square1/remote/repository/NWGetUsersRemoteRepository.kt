package io.square1.remote.repository

import io.reactivex.Observable
import io.square1.remote.model.Results

interface NWGetUsersRemoteRepository {
    fun getUsers(): Observable<Results>
}