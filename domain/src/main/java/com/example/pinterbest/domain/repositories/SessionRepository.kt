package com.example.pinterbest.domain.repositories

interface SessionRepository {
    fun saveUserId(id: Int)

    fun saveSession(cookie: String)

    fun authProvider(): String?
}
