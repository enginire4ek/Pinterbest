package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.IdEntity
import com.google.gson.annotations.SerializedName

data class DataIdEntity(
    @SerializedName("ID")
    val id: Int
)

fun DataIdEntity.toIdEntity(): IdEntity {
    return IdEntity(
        id = id
    )
}
