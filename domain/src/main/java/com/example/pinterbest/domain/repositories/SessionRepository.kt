package com.example.pinterbest.domain.repositories

interface SessionRepository {
    fun saveSession(cookie: String)

    fun authProvider(): String?
}
