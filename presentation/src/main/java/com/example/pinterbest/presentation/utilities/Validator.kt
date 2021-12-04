package com.example.pinterbest.presentation.utilities

import android.util.Patterns
import android.widget.EditText
import com.example.pinterbest.presentation.R
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class Validator(resource: ResourceProvider) {
    private val emailValidMessage = resource.getString(R.string.validation_username)
    private val nameValidMessage = resource.getString(R.string.validation_username)
    private val passwordPolicy = resource.getString(R.string.validation_password)

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
            val error: String? = if (valid) null else nameValidMessage
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
            val error: String? = if (valid) null else emailValidMessage
            setError(data, error)
        }

        return valid
    }

    /**
     * Checks if the password is valid as per the following password policy.
     * Password should be minimum minimum 8 characters long.
     * Password should contain at least one number.
     */
    fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
        val str = getText(data)
        var valid = true

        // Password policy check
        // Password should be minimum minimum 8 characters long
        if (str.length < password_min_size) {
            valid = false
        }
        // Password should contain at least one number
        val exp = ".*[0-9].*"
        val pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Set error if required
        if (updateUI) {
            val error: String? = if (valid) null else passwordPolicy
            setError(data, error)
        }

        return valid
    }

    companion object {
        const val password_min_size = 8
    }
}
