package com.example.pinterbest.utilities

import android.content.Context
import android.content.SharedPreferences
import com.example.pinterbest.R

class SessionManager(context: Context) {

    // Session manager to save and fetch data from SharedPreferences
    private var prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.login_info),
        Context.MODE_PRIVATE
    )

    companion object {
        const val USER_DATA = "user_data"
        const val COOKIE = "cookie"
        const val STATUS = "status"
    }

    // Function to save user data
    fun saveUserData(cookie: String, username: String, password: String, status: Boolean) {
        val editor = prefs.edit()
        editor.putString(COOKIE, cookie)
        editor.putStringSet(USER_DATA, hashSetOf(username, password))
        editor.putBoolean(STATUS, status)
        editor.apply()
    }

    // Function to fetch user status
    fun fetchUserStatus(): Boolean {
        return prefs.getBoolean(STATUS, false)
    }

    // Function to fetch user data
    fun fetchUserData(): MutableSet<String>? {
        return prefs.getStringSet(USER_DATA, HashSet<String>())
    }

    // Function to fetch cookie
    fun fetchCookie(): String? {
        return prefs.getString(COOKIE, null)
    }
}
