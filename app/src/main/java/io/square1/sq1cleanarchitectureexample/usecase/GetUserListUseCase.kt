package io.square1.sq1cleanarchitectureexample.usecase

import android.annotation.SuppressLint
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import io.square1.remote.model.NWUserModel
import io.square1.remote.model.Results
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.storage.model.Picture
import io.square1.storage.model.RLMUserModel
import io.square1.storage.repository.RLMGetUsersStorageRepository
import javax.inject.Inject


class GetUserListUseCase @Inject constructor(
    private val nwGetUserRemoteRepository: NWGetUsersRemoteRepository,
    private val rlmGetUsersStorageRepository: RLMGetUsersStorageRepository
) {
    // Ask for users to API and insert new ones in DDBB
    fun getUsers(): Flowable<List<RLMUserModel>> {
        // Must to return current realm data and send request to API
        addUsersToRealm(nwGetUserRemoteRepository.getUsers())
        return getUsersFromRealm()
    }

    private val userList: ArrayList<RLMUserModel> = ArrayList()

    // Insert new users on DDBB (Realm)
    @SuppressLint("CheckResult")
    fun addUsersToRealm(users: Observable<Results>) {
        users.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { users ->
            for (user: NWUserModel in users.results) {
                rlmGetUsersStorageRepository.addUsers(
                    RLMUserModel(
                        user.email,
                        Picture(user.picture.large, user.picture.medium, user.picture.thumbnail)
                    )
                )
            }
        }
    }

    fun getUsersFromRealm(): Flowable<List<RLMUserModel>> {
        return rlmGetUsersStorageRepository.getUsers()
    }


}