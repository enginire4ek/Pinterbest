package com.example.pinterbest.utilities

import android.content.res.Resources

class ResourceProvider(private var resources: Resources) {

    fun getString(stringResId: Int): String {
        return resources.getString(stringResId)
    }
}
