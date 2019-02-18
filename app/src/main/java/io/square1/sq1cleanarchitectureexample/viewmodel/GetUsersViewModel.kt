package io.square1.sq1cleanarchitectureexample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.square1.remote.repository.NWGetUsersRemoteRepository
import io.square1.sq1cleanarchitectureexample.usecase.GetUserListUseCase
import io.square1.storage.model.RLMUserModel
import io.square1.storage.repository.RLMGetUsersStorageRepository


class GetUsersViewModel constructor(
    private val rlmGetUsersStorageRepository: RLMGetUsersStorageRepository,
    private val nwGetUsersRemoteRepository: NWGetUsersRemoteRepository
) : ViewModel() {

    private var getUserListUseCase: GetUserListUseCase


    var flowableToLiveData: LiveData<List<RLMUserModel>>

    init {
        getUserListUseCase = GetUserListUseCase(nwGetUsersRemoteRepository, rlmGetUsersStorageRepository)
        val rxFlowable: Flowable<List<RLMUserModel>> = Flowable.fromPublisher(getUsers())
        flowableToLiveData = LiveDataReactiveStreams.fromPublisher(rxFlowable)
    }

    fun getUsers(): Flowable<List<RLMUserModel>>? {
        return getUserListUseCase.getUsers()
    }
}