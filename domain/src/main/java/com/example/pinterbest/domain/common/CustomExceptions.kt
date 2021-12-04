package com.example.pinterbest.domain.common

import java.lang.IllegalStateException

class AlreadyAuthorizedException : IllegalStateException()
class InvalidDataException : IllegalStateException()
class UserExistsException : IllegalStateException()
class WrongPasswordException : IllegalStateException()
class UserNotFoundException : IllegalStateException()
