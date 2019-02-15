package io.square1.remote.model

data class Results(val results: ArrayList<NWUserModel> = ArrayList())

data class NWUserModel(
    val email: String,
    val picture: Picture
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)