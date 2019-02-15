package io.square1.storage.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RLMUserModel(
    @PrimaryKey
    var email: String = "EMAIL",
    var picture: Picture? = Picture()
) : RealmObject()

open class Picture(
    var large: String = "",
    var medium: String = "",
    var thumbnail: String = ""
) : RealmObject()