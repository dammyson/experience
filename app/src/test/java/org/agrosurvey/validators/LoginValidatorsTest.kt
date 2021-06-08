package org.agrosurvey.validators

import com.google.common.truth.Truth.assertThat
import org.agrosurvey.domain.entities.get.isBearerTokenValid
import org.junit.Test

class LoginValidatorsTest {

    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat(LoginValidators.isValidEmail("name_@email.com")).isTrue()
    }

    @Test
    fun loginValidators_CorrectEmailSubDomain_ReturnsTrue() {
        assertThat(LoginValidators.isValidEmail("name@email.co.uk")).isTrue()
    }

    @Test
    fun loginValidators_InvalidEmailNoTld_ReturnsFalse() {
        assertThat(LoginValidators.isValidEmail("name@email")).isFalse()
    }

    @Test
    fun loginValidators_InvalidEmailDoubleDot_ReturnsFalse() {
        assertThat(LoginValidators.isValidEmail("name@email..com")).isFalse()
    }

    @Test
    fun loginValidators_InvalidEmailNoUsername_ReturnsFalse() {
        assertThat(LoginValidators.isValidEmail("@email.com")).isFalse()
    }

    @Test
    fun loginValidators_EmptyString_ReturnsFalse() {
        assertThat(LoginValidators.isValidEmail("")).isFalse()
    }

    @Test
    fun loginValidators_NullEmail_ReturnsFalse() {
        assertThat(LoginValidators.isValidEmail("null")).isFalse()
    }


    @Test
    fun passwordValidator_NullPassword_ReturnsFalse() {
        assertThat(LoginValidators.isValidPassword("null")).isTrue()
    }

    @Test
    fun passwordValidator_BlankPassword_ReturnsFalse() {
        assertThat(LoginValidators.isValidPassword("           ")).isFalse()
    }

    @Test
    fun passwordValidator_NotBlankPassword_ReturnsTrue() {
        assertThat(LoginValidators.isValidPassword("15eeb9E7")).isTrue()
    }

    @Test
    fun textFieldsToFormData_CorrectObject_ReturnsTrue() {
        val data = LoginValidators.textFieldsToFormData("name@email.com", "password")
        assertThat(data).isNotNull()
        assertThat(data!!.email).isEqualTo("name@email.com")
        assertThat(data.password).isEqualTo("password")
    }

    @Test
    fun textFieldsToFormData_WrongObject_ReturnsNull() {
        val data = LoginValidators.textFieldsToFormData("password", "password@email.com")
        assertThat(data).isNull()
    }

    @Test
    fun bearerTokenValidator_CorrectBearerToken_ReturnsTrue() {
        val token = "Bearer 1KKmr7JbYMF2Lx5UjSDjIchrseRBl81H0Dv7MCkf"
        assertThat(isBearerTokenValid(token)).isTrue()
    }

    @Test
    fun bearerTokenValidator_SmallCaseBearerToken_ReturnsFalse() {
        val token = "bearer 1KKmr7JbYMF2Lx5UjSDjIchrseRBl81H0Dv7MCkf"
        assertThat(isBearerTokenValid(token)).isFalse()
    }

    @Test
    fun bearerTokenValidator_WrongSyntaxNoSpaceBearerToken_ReturnsFalse() {
        val token = "Bearer1KKmr7JbYMF2Lx5UjSDjIchrseRBl81H0Dv7MCkf"
        assertThat(isBearerTokenValid(token)).isFalse()
    }

    @Test
    fun bearerTokenValidator_WrongSyntaxMisplacedSpaceBearerToken_ReturnsFalse() {
        val token = "Bearer1KKmr7JbY MF2Lx5UjSDjIchrseRBl81H0Dv7MCkf"
        assertThat(isBearerTokenValid(token)).isFalse()
    }

    @Test
    fun bearerTokenValidator_2nd_CorrectSyntaxMisplacedSpaceBearerToken_ReturnsTrue() {
        val token = "Bearer  DjIchrseRBl81H0Dv7MCkf"
        assertThat(isBearerTokenValid(token)).isTrue()
    }

    @Test
    fun bearerTokenValidator_2rd_BlankdSpaceBearerToken_ReturnsTrue() {
        val token = "Bearer "
        assertThat(isBearerTokenValid(token)).isTrue()
    }

    @Test
    fun bearerTokenValidator_3rd_blankSpaceBearerToken_ReturnsTrue() {
        val token = "Bearer  "
        assertThat(isBearerTokenValid(token)).isTrue()
    }

    @Test
    fun bearerTokenValidator_4rd_WrongSyntaxMisplacedSpaceBearerToken_ReturnsFalse() {
        val token = "  "
        assertThat(isBearerTokenValid(token)).isFalse()
    }
}