package com.example.pinterbest.presentation.utilities

import com.example.pinterbest.domain.common.AlreadyAuthorizedException
import com.example.pinterbest.domain.common.InvalidDataException
import com.example.pinterbest.domain.common.UserExistsException
import com.example.pinterbest.domain.common.UserNotFoundException
import com.example.pinterbest.domain.common.WrongPasswordException
import com.example.pinterbest.presentation.R

object ErrorMessageGenerator {
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
