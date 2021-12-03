package com.example.pinterbest.domain.common

import java.lang.IllegalStateException

class AlreadyAuthorizedException : IllegalStateException()
class InvalidDataException : IllegalStateException()
class UserExistsException : IllegalStateException()
class WrongPasswordException : IllegalStateException()
class UserNotFoundException : IllegalStateException()

object ErrorMessage {
    const val INVALID_DATA = 400
    const val WRONG_PASSWORD = 401
    const val ALREADY_AUTHORIZED = 403
    const val USER_NOT_FOUND = 404
    const val USER_EXISTS = 409

    val ErrorMap = mapOf(
        INVALID_DATA to InvalidDataException(),
        WRONG_PASSWORD to WrongPasswordException(),
        ALREADY_AUTHORIZED to AlreadyAuthorizedException(),
        USER_NOT_FOUND to UserNotFoundException(),
        USER_EXISTS to UserExistsException(),
    )
}
