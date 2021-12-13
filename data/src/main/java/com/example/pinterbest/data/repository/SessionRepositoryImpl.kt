package com.example.pinterbest.data.repository

import android.content.SharedPreferences
import com.example.pinterbest.domain.repositories.SessionRepository

class SessionRepositoryImpl(private val preferences: SharedPreferences) : SessionRepository {
    companion object {
        const val COOKIE = "cookie"
    }

    override fun saveUserId(id: Int) {
        val editor = preferences.edit()
        editor.putInt(COOKIE, id)
        editor.apply()
    }

    override fun saveSession(cookie: String) {
        val editor = preferences.edit()
        editor.putString(COOKIE, cookie)
        editor.apply()
    }

    override fun authProvider(): String? {
        return preferences.getString(COOKIE, null)
    }
}
