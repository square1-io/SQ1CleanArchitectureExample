package io.square1.sq1cleanarchitectureexample.mappers

import io.square1.sq1cleanarchitectureexample.model.Picture
import io.square1.sq1cleanarchitectureexample.model.UIUserModel
import io.square1.storage.model.RLMUserModel

class BidirectionalMapper {

    fun mapToUI(rlmUserModel: RLMUserModel): UIUserModel {
        return UIUserModel(rlmUserModel.email, mappedPicture(rlmUserModel.picture))
    }

    private fun mappedPicture(picture: io.square1.storage.model.Picture?): Picture? {
        return picture?.let { Picture(it.large, it.medium, it.thumbnail) } ?: Picture()
    }
}