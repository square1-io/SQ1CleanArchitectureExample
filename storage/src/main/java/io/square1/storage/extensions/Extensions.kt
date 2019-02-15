package io.square1.storage.extensions

/**
 * Created by ricar on 24/8/17.
 */


import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

/*
fun <T : RealmObject> queryAllSortedByFieldAsObservable(clazz: Class<T>, field: String): Flowable<List<T>> {
    return realmTransactionAsFlowable { realm ->
        realm.where(clazz)
                .findAllAsync().sort(field, Sort.DESCENDING)
                .filter { it.isLoaded }
                .map { realm.copyFromRealm(it) }
    }
}


fun <T : RealmObject> queryAsObservable(clazz: Class<T>, query: (RealmQuery<T>) -> Unit): Observable<List<T>> {
    return realmTransactionAsFlowable { realm ->
        val realmQuery = RealmQuery.createQuery(realm, clazz)
        query(realmQuery)
        realmQuery.findAllAsync()
                .filter { it.isLoaded }
                .map { realm.copyFromRealm(it) }
    }
}

fun <T : RealmObject> queryFirst(clazz: Class<T>, query: (RealmQuery<T>) -> Unit): T? {
    Realm.getDefaultInstance().use {
        val realmQuery = RealmQuery.createQuery(it, clazz)
        query(realmQuery)
        val item : T? = realmQuery.findFirst()
        return if(item != null && item.isValid) it.copyFromRealm(item) else null
    }
}

fun <T : RealmObject> queryFirstAsObservable(clazz: Class<T>, query: (RealmQuery<T>) -> Unit): Observable<T> {
    return realmTransactionAsFlowable { realm ->
        val realmQuery = RealmQuery.createQuery(realm, clazz)
        query(realmQuery)
        realm.copyFromRealm(realmQuery.findFirst())
    }
}

fun <T : RealmObject> T.create() {
    Realm.getDefaultInstance().transaction {
        it.copyToRealm(this)
    }
}

fun <T : RealmObject> T.createManaged(): Observable<T> {
    return realmTransactionAsFlowable { it.copyToRealm(this) }
}

fun <T : RealmObject> T.createOrUpdate() {
    Realm.getDefaultInstance().transaction { it.copyToRealmOrUpdate(this) }
}


fun <T : RealmObject> T.createOrUpdateManaged(): Observable<T> {
    return realmTransactionAsFlowable { it.copyToRealmOrUpdate(this) }
}

*/
fun <T : RealmObject> T.save() {
    Realm.getDefaultInstance().transaction { realm ->
        if (this.hasPrimaryKey(realm)) realm.copyToRealmOrUpdate(this) else realm.copyToRealm(this)
    }
}


fun <T : RealmObject> T.saveManaged(): Completable {
    return Realm.getDefaultInstance().completableTransaction { realm ->
        if (this.hasPrimaryKey(realm)) realm.copyToRealmOrUpdate(this) else realm.copyToRealm(this)
    }
}

fun <T : Collection<RealmObject>> T.saveAll() {
    Realm.getDefaultInstance().transaction {
        it.copyToRealmOrUpdate(this)
    }
}

fun <T : RealmObject> Collection<T>.saveAllManaged(): Completable {
    return Realm.getDefaultInstance().completableTransaction { realm ->
        realm.copyToRealmOrUpdate(this)
    }
}

fun Array<out RealmObject>.saveAll() {
    val realm = Realm.getDefaultInstance()
    realm.transaction {
        forEach { if (it.hasPrimaryKey(realm)) realm.copyToRealmOrUpdate(it) else realm.copyToRealm(it) }
    }
    realm.refresh()
}


fun <T : RealmObject> Array<T>.saveAllManaged(): Completable {
    val realm = Realm.getDefaultInstance()
    return realm.completableTransaction { realmInstance ->
        forEach {
            if (it.hasPrimaryKey(realmInstance))
                realmInstance.copyToRealmOrUpdate(it)
            else realmInstance.copyToRealm(it)
        }
    }.doOnComplete { realm.refresh() }
}

fun <T : RealmObject> deleteAllObjectsFromRealm(clazz: Class<T>) {
    Realm.getDefaultInstance().transaction {
        val result = it.where(clazz).findAll()
        result.deleteAllFromRealm()
    }
}

fun <T : RealmObject> T.delete() {
    Realm.getDefaultInstance().transaction {
        this.deleteFromRealm()
    }
}

fun <T : RealmObject> T.deleteManaged(): Completable {
    return Realm.getDefaultInstance().completableTransaction {
        this.deleteFromRealm()
    }
}

fun <T : RealmObject> T.deleteQuery(myQuery: () -> RealmResults<T>) {
    Realm.getDefaultInstance().transaction {
        myQuery.invoke().deleteAllFromRealm()
    }
}

fun <T : RealmObject> RealmResults<T>.deleteAllTransaction() {
    Realm.getDefaultInstance().transaction {
        this.deleteAllFromRealm()
    }
}

fun <T : RealmObject> RealmResults<T>.deleteAllManaged(): Completable {
    return Realm.getDefaultInstance().completableTransaction {
        this.deleteAllFromRealm()
    }
}

inline fun <reified T : RealmObject> T.count(): Long {
    val realm = Realm.getDefaultInstance()
    return realm.where(T::class.java).count()
}


/**
 * UTILITY METHODS
 */

@Synchronized
private fun Realm.transaction(action: (Realm) -> Unit) {
    use {
        executeTransaction { action(this) }
    }
}

private fun Realm.completableTransaction(action: (Realm) -> Unit): Completable =
    Completable.create { subscriber ->
        try {
            use {
                executeTransaction { action(this) }
                subscriber.onComplete()
            }
        } catch (e: Exception) {
            subscriber.onError(e)
        }
    }.addRealmSchedulers()

/*
private fun <T> T.withQuery(block: (T) -> Unit): T {
    block(this); return this
}
*/
private fun <T : RealmObject> T.hasPrimaryKey(realm: Realm): Boolean {
    if (realm.schema.get(this.javaClass.simpleName) == null) {
        throw IllegalArgumentException(this.javaClass.simpleName +
                " is not part of the schema for this Realm. Did you added realm-android plugin in your build.gradle file?")
    }
    return realm.schema.get(this.javaClass.simpleName)?.hasPrimaryKey() ?: false
}

class BackgroundThread : HandlerThread("Scheduler-Realm-BackgroundThread",
    Process.THREAD_PRIORITY_BACKGROUND)

fun <T : RealmModel> RealmResults<T>.toFlowableList(realm: Realm): Flowable<List<T>> {
    return this.asFlowable()
        .filter(RealmResults<T>::isLoaded)
        .map { realm.copyFromRealm(it) }
        .addRealmSchedulers()
}

val looper: Looper = if (Looper.myLooper() != Looper.getMainLooper()) {
    val backgroundThread = BackgroundThread()
    backgroundThread.start()
    backgroundThread.looper
} else {
    Looper.getMainLooper()
}

fun <T> Flowable<T>.addRealmSchedulers(): Flowable<T> =
    this.unsubscribeOn(AndroidSchedulers.from(looper))
        .subscribeOn(AndroidSchedulers.from(looper))


fun Completable.addRealmSchedulers(): Completable =
    this.unsubscribeOn(AndroidSchedulers.from(looper))
        .subscribeOn(AndroidSchedulers.from(looper))

fun <T> Single<T>.addRealmSchedulers(): Single<T> =
    this.unsubscribeOn(AndroidSchedulers.from(looper))
        .subscribeOn(AndroidSchedulers.from(looper))

fun <T> Flowable<T>.addRealmObserveSchedulers(): Flowable<T> =
    this.observeOn(AndroidSchedulers.from(looper))

fun Completable.addRealmObserveSchedulers(): Completable =
    this.observeOn(AndroidSchedulers.from(looper))


fun <T> Single<T>.addRealmObserveSchedulers(): Single<T> =
    this.observeOn(AndroidSchedulers.from(looper))