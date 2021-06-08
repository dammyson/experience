package org.agrosurvey.validators

import org.agrosurvey.domain.entities.post.LoginPost
import org.agrosurvey.features.login.AuthFormData
import java.util.regex.Pattern

object LoginValidators {

    fun isValidEmail(email: String): Boolean {
        /**
         * Email validation pattern.
         */
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.isNotBlank()
    }

    fun textFieldsToFormData(email: String, password: String): AuthFormData? {
        return if (isValidEmail(email) && isValidPassword(password)) {
            AuthFormData(email = email, password = password)
        } else null
    }

    fun AuthFormData.toLoginPost(): LoginPost {
        return LoginPost(email = email, password = password)
    }

}