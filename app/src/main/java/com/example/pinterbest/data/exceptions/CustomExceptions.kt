package com.example.pinterbest.data.exceptions

import java.lang.IllegalStateException

class AlreadyAuthorizedException : IllegalStateException()
class InvalidDataException : IllegalStateException()
class UserExistsException : IllegalStateException()
class WrongPasswordException : IllegalStateException()
class UserNotFoundException : IllegalStateException()
