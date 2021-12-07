package com.example.pinterbest.presentation.common

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import java.io.File
import java.io.InputStream

val Context.appComponent: ApplicationApi
    get() = when (this) {
        is ApplicationComponentHolder -> getApplicationApi()
        else -> this.applicationContext.appComponent
    }

fun Fragment.getAppComponent() = requireContext().appComponent

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}