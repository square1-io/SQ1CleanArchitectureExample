package io.square1.sq1cleanarchitectureexample.model

data class UIUserModel(
    val email: String = "",
    val picture: Picture?
)

data class Picture(
    val large: String = "",
    val medium: String = "",
    val thumbnail: String = ""
)