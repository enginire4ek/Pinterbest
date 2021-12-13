package com.example.pinterbest.domain.entities

data class Board(
    val ID: Int,
    val avatarLink: String,
    val description: String,
    val title: String,
    val userID: Int
) {
    override fun toString(): String {
        return this.title
    }
}
