package com.example.pinterbest.utilities

import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

object Validator {
    private const val PASSWORD_MIN_SIZE = 8
    private val PASSWORD_POLICY =
        """Пароль должен состоять минимум из 8 символов, а также
            |- должен содержать хотя бы одну заглавную букву,
            |- хотя бы одну строчную букву,
            |- хотя бы одну цифру и
            |- хотя бы один специальный символ среди ~!@#$%^&*()-_=+|[]{};:'\",<.>/?""".trimMargin()

    private const val NAME_VALIDATION_MSG = "Введите корректное имя"
    private const val EMAIL_VALIDATION_MSG = "Введите корректный электронный адрес"

    private fun getText(data: Any): String {
        var str = ""
        if (data is EditText) {
            str = data.text.toString()
        } else if (data is String) {
            str = data
        }
        return str
    }

    private fun setError(data: Any, error: String?) {
        if (data is EditText) {
            if (data.parent.parent is TextInputLayout) {
                (data.parent.parent as TextInputLayout).error = error
            } else {
                data.setError(error)
            }
        }
    }

    /**
     * Checks if the name is valid.
     * @param data - can be EditText or String
     * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
     * @return - true if the name is valid.
     */
    fun isValidName(data: Any, updateUI: Boolean = true): Boolean {
        val str = getText(data)
        val valid = str.trim().length > 2

        // Set error if required
        if (updateUI) {
            val error: String? = if (valid) null else NAME_VALIDATION_MSG
            setError(data, error)
        }

        return valid
    }

    /**
     * Checks if the email is valid.
     * @param data - can be EditText or String
     * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
     * @return - true if the email is valid.
     */
    fun isValidEmail(data: Any, updateUI: Boolean = true): Boolean {
        val str = getText(data)
        val valid = Patterns.EMAIL_ADDRESS.matcher(str).matches()

        // Set error if required
        if (updateUI) {
            val error: String? = if (valid) null else EMAIL_VALIDATION_MSG
            setError(data, error)
        }

        return valid
    }

    /**
     * Checks if the password is valid as per the following password policy.
     * Password should be minimum minimum 8 characters long.
     * Password should contain at least one number.
     * Password should contain at least one capital letter.
     * Password should contain at least one small letter.
     * Password should contain at least one special character.
     * Allowed special characters: "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
     */
    fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
        val str = getText(data)
        var valid = true

        // Password policy check
        // Password should be minimum minimum 8 characters long
        if (str.length < PASSWORD_MIN_SIZE) {
            valid = false
        }
        // Password should contain at least one number
        var exp = ".*[0-9].*"
        var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
        var matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one capital letter
        exp = ".*[A-Z].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one small letter
        exp = ".*[a-z].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one special character
        // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
        exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Set error if required
        if (updateUI) {
            val error: String? = if (valid) null else PASSWORD_POLICY
            setError(data, error)
        }

        return valid
    }
}
