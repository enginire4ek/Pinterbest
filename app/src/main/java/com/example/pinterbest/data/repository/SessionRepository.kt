package com.example.pinterbest.data.repository

import android.content.SharedPreferences

class SessionRepository(private val preferences: SharedPreferences) {

    companion object {
        const val COOKIE = "cookie"
    }

    // Function to save user data
    fun saveUserData(cookie: String) {
        val editor = preferences.edit()
        editor.putString(COOKIE, cookie)
        editor.apply()
    }

    // Function to fetch cookie
    fun fetchCookie(): String? {
        return preferences.getString(COOKIE, null)
    }
}
