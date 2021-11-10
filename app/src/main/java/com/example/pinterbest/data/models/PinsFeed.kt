package com.example.pinterbest.data.models

import com.example.pinterbest.adapters.PinObjectViewData
import com.example.pinterbest.adapters.PinsFeedViewData

data class PinsFeed(val pins: List<PinObject>) {
    fun mapToViewData(): PinsFeedViewData {
        return PinsFeedViewData(
            pins.map {
                PinObjectViewData(
                    it.imageWidth,
                    it.imageHeight,
                    it.imageLink,
                    it.title,
                    it.imageAvgColor
                )
            }
        )
    }
}
