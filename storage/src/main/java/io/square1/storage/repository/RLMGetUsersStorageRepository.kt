package io.square1.storage.repository

import io.reactivex.Flowable
import io.reactivex.Observable
import io.square1.storage.model.RLMUserModel

interface RLMGetUsersStorageRepository {

    fun getUsers(): Flowable<List<RLMUserModel>>
    fun addUsers(user: RLMUserModel)
}