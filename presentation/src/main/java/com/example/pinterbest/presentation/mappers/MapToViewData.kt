package com.example.pinterbest.presentation.mappers

import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.presentation.models.PinObjectViewData
import com.example.pinterbest.presentation.models.PinsListViewData
import com.example.pinterbest.presentation.models.ProfileViewData

object MapToViewData {
    fun mapToPinsListViewData(pinsList: PinsList): PinsListViewData {
        return PinsListViewData(
            pinsList.pins.map {
                PinObjectViewData(
                    it.userID,
                    it.description,
                    it.imageWidth,
                    it.imageHeight,
                    it.imageLink,
                    it.title,
                    it.imageAvgColor
                )
            }
        )
    }

    fun mapToProfileViewData(profile: Profile): ProfileViewData {
        return ProfileViewData(
            profile.username,
            profile.avatarLink,
            profile.following,
            profile.followers,
            profile.boardsCount,
            profile.pinsCount
        )
    }
}