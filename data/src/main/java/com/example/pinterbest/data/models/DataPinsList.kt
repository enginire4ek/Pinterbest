package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.PinsList

data class DataPinsList(
    val pins: List<DataPinObject>
)

fun DataPinsList.toPinsList(): PinsList {
    return PinsList(
        pins = pins.map {
            it.toPin()
        }
    )
}
