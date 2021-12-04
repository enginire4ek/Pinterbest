package com.example.pinterbest.data.common

import com.example.pinterbest.domain.common.AlreadyAuthorizedException
import com.example.pinterbest.domain.common.UserExistsException
import com.example.pinterbest.domain.common.InvalidDataException
import com.example.pinterbest.domain.common.UserNotFoundException
import com.example.pinterbest.domain.common.WrongPasswordException

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
