package org.agrosurvey

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.agrosurvey.data.Logs
import org.agrosurvey.domain.entities.get.Organisation
import org.agrosurvey.domain.entities.get.User
import org.agrosurvey.domain.entities.get.getBearToken
import org.agrosurvey.domain.entities.get.getBearerToken
import java.lang.reflect.Type
import javax.inject.Inject


class PreferenceHelper @Inject constructor(val sp: SharedPreferences) {
    private val gson by lazy { Gson() }


    fun saveUserDetails(user: User, organisations: List<Organisation>): Boolean {
        return user.run {
            setAuthStatus(true) &&
                    setAccessToken(getBearToken()) &&
                    setToken(getBearerToken()) &&
                    setCurrentUserID(id) &&
                    setUsername(username) &&
                    saveUserProfile(user) &&
                    saveUserOrganisations(organisations)
        }
    }


    fun credentialsAreValid(): Boolean {
        return isAuthenticated() && getUserProfile() != null && getUserOrganisations().isNullOrEmpty()
            .not()
    }

    fun isAuthenticated(): Boolean = sp.getBoolean(AUTH_STATUS_KEY, false)

    private fun setAuthStatus(authenticated: Boolean): Boolean {
        return sp.edit().putBoolean(AUTH_STATUS_KEY, authenticated).commit()
    }

    private fun setAccessToken(token: String): Boolean {
        return sp.edit().putString(ACCESS_TOKEN_KEY, token).commit()
    }

    private fun setToken(token: String): Boolean {
        return sp.edit().putString(TOKEN_KEY, token).commit()
    }

    private fun setCurrentUserID(id: Int): Boolean {
        return sp.edit().putInt(USER_ID_KEY, id).commit()
    }

    private fun setUsername(username: String): Boolean {
        return sp.edit().putString(USERNAME_KEY, username).commit()
    }

    private fun saveUserProfile(user: User) =
        sp.edit().putString(USER_PROFILE_KEY, gson.toJson(user)).commit()

    private fun saveUserOrganisations(organisations: List<Organisation>) =
        sp.edit().putString(USER_ORGS_KEY, gson.toJson(organisations)).commit()


    fun getUsername(): String =
        sp.getString(USERNAME_KEY, "").toString()

    fun getAccessToken(): String {
        return sp.getString(ACCESS_TOKEN_KEY, EMPTY_ACCESS_TOKEN).toString()
    }

    fun getToken(): String {
        return sp.getString(TOKEN_KEY, EMPTY_ACCESS_TOKEN).toString()
    }

    fun getUserProfile(): User? {
        val jsonArray = sp.getString(USER_PROFILE_KEY, "").toString()
        return if (jsonArray.isBlank()) null
        else {
            val listType: Type = object : TypeToken<User>() {}.type
            try {
                val user: User = Gson().fromJson(jsonArray, listType)
                Logs.logCat("$PREFS_TAG Parsed user : $user", false)
                user
            } catch (parsingError: JsonParseException) {
                Logs.logCat("$PREFS_TAG user parsing failed")
                null
            } catch (syntaxError: JsonSyntaxException) {
                Logs.logCat("$PREFS_TAG user syntax not correct")
                null
            }
        }
    }

    fun getUserOrganisations(): List<Organisation>? {
        val jsonArray = sp.getString(USER_ORGS_KEY, "").toString()
        return if (jsonArray.isBlank()) {
            Logs.logCat("$PREFS_TAG No user organisation saved before", false)
            null
        } else {
            val listType: Type = object : TypeToken<ArrayList<Organisation>>() {}.type
            try {
                val orgs: List<Organisation> = Gson().fromJson(jsonArray, listType)
                Logs.logCat("$PREFS_TAG Parsed user orgs : $orgs", false)
                orgs
            } catch (parsingError: JsonParseException) {
                Logs.logCat("$PREFS_TAG user organisations parsing failed")
                null
            } catch (syntaxError: JsonSyntaxException) {
                Logs.logCat("$PREFS_TAG user organisations syntax not correct")
                null
            }
        }
    }

    fun getUserID(): String = sp.getInt(USER_ID_KEY, EMPTY_ID).toString()


    private val authLiveStatus = MutableLiveData(resolveAuthStatus())


    fun whenAuthStatusChanged(): LiveData<AuthStatus> {
        sp.registerOnSharedPreferenceChangeListener(authStatusPrefListener)
        return authLiveStatus
    }

    private val authStatusPrefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == AUTH_STATUS_KEY) authLiveStatus.postValue(resolveAuthStatus())
        }

    private fun resolveAuthStatus() =
        if (isAuthenticated()) AuthStatus.AUTHENTICATED else AuthStatus.NOT_AUTHENTICATED

    companion object {

        private const val AUTH_STATUS_KEY = "auth_status"

        private const val ACCESS_TOKEN_KEY = "access_token"

        private const val TOKEN_KEY = "a_token"

        private const val USERNAME_KEY = "user_name"

        private const val USER_PROFILE_KEY = "current_user"

        private const val USER_ORGS_KEY = "user_organisations"

        private const val USER_ID_KEY = "user_id"

        const val EMPTY_ACCESS_TOKEN = ""

        const val EMPTY_ID = -1
    }

    private val PREFS_TAG = "Prefs"
}


enum class AuthStatus { AUTHENTICATED, NOT_AUTHENTICATED }