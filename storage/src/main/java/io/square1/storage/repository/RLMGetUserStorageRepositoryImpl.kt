package io.square1.storage.repository

import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import io.square1.storage.extensions.addRealmSchedulers
import io.square1.storage.model.RLMUserModel


class RLMGetUserStorageRepositoryImpl(private val realm: Realm) : RLMGetUsersStorageRepository {
    override fun addUsers(user: RLMUserModel) {
        realm.executeTransaction { realm ->
            realm.copyToRealmOrUpdate(user)
        }
    }

    override fun getUsers(): Flowable<List<RLMUserModel>> {
        // Query Realm for all users
        val users = realm.where(RLMUserModel::class.java).findAll().toFlowableList(realm)
        return users
    }

    fun <T : RealmModel> RealmResults<T>.toFlowableList(realm: Realm): Flowable<List<T>> {
        return this.asFlowable()
            .filter(RealmResults<T>::isLoaded)
            .map { realm.copyFromRealm(it) }
            .addRealmSchedulers()
    }
}