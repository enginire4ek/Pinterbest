package com.example.pinterbest.data.exceptions

import com.example.pinterbest.R
import java.lang.IllegalStateException

class AlreadyAuthorizedException : IllegalStateException()
class InvalidDataException : IllegalStateException()
class UserExistsException : IllegalStateException()
class WrongPasswordException : IllegalStateException()
class UserNotFoundException : IllegalStateException()

object ErrorMessage {
    fun <T> processErrorCode(t: T): Int {
        return when (t) {
            is AlreadyAuthorizedException -> R.string.error_already_authorized
            is InvalidDataException -> R.string.error_invalid_data
            is WrongPasswordException -> R.string.error_wrong_password
            is UserNotFoundException -> R.string.error_user_not_found
            is UserExistsException -> R.string.error_user_exists
            else -> R.string.apology_text
        }
    }
}
