package com.example.pinterbest.adapters

import com.example.pinterbest.data.models.PinObject

data class PinObjectViewData(val pinObject: PinObject) {
    var imageLink = pinObject.imageLink
    var title = pinObject.title
    var imageAvgColor = pinObject.imageAvgColor
}
