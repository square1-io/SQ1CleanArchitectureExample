package io.square1.sq1cleanarchitectureexample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.realm.Realm
import io.square1.remote.repository.NWGetUserRemoteRepositoryImpl
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.sq1cleanarchitectureexample.usecase.GetUserListUseCase
import io.square1.storage.model.RLMUserModel
import io.square1.storage.repository.RLMGetUserStorageRepositoryImpl
import io.square1.storage.repository.RLMGetUsersStorageRepository


class GetUsersViewModel : ViewModel() {

    private var getUserListUseCase: GetUserListUseCase


    var flowableToLiveData: LiveData<List<RLMUserModel>>

    init {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        // delete all realm objects
        realm.deleteAll()
        //commit realm changes
        realm.commitTransaction()
        val nwGetUsersRemoteRepository: NWGetUsersRemoteRepository = NWGetUserRemoteRepositoryImpl()
        val rlmGetUsersStorageRepository: RLMGetUsersStorageRepository = RLMGetUserStorageRepositoryImpl(realm)
        getUserListUseCase = GetUserListUseCase(nwGetUsersRemoteRepository, rlmGetUsersStorageRepository)
        val rxFlowable: Flowable<List<RLMUserModel>> = Flowable.fromPublisher(getUsers())
        flowableToLiveData = LiveDataReactiveStreams.fromPublisher(rxFlowable)

    }

    fun getUsers(): Flowable<List<RLMUserModel>>? {
        return getUserListUseCase.getUsers()
    }
}