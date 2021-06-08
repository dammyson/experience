package org.agrosurvey.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.agrosurvey.PreferenceHelper
import org.agrosurvey.data.Logs
import org.agrosurvey.data.remote.*
import org.agrosurvey.data.remote.auth.AuthRepository
import org.agrosurvey.domain.entities.get.Organisation
import org.agrosurvey.domain.entities.get.User
import org.agrosurvey.domain.entities.get.getBearToken
import org.agrosurvey.domain.entities.get.isBearerTokenValid
import org.agrosurvey.validators.LoginValidators.toLoginPost
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository,
    val preferences: PreferenceHelper
) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val onRequestFailed = MutableLiveData<Pair<String, ApiErrorType>>()
    val onRequestSuccessful = MutableLiveData<User>()

    fun login(input: AuthFormData) {
        isLoading.postValue(true)
        val payload = input.toLoginPost()
        Logs.logCat("LOGIN PAYLOAD", false)
        Logs.logCat(payload, true)

        repository.signIn(payload) {

            when (it) {
                is ApiEmptyResponse -> {
                    isLoading.postValue(false)
                    val msg = Pair("No content", ApiErrorType.UNKNOWN_ERROR)
                    onRequestFailed.postValue(msg)
                    Logs.logCat(msg, true)
                }
                is ApiErrorResponse -> with(it) {
                    isLoading.postValue(false)

                    handleLoginErrorApiResponse(it)
                }
                is ApiSuccessResponse -> {
                    val user: User = it.body
                    Logs.logCat("USER  BEARER TOKEN : ${user.getBearToken()}", false)
                    if (isBearerTokenValid(user.getBearToken())) {
                        getUserOrganisations(user)
                    } else {
                        isLoading.postValue(false)
                        handleLoginErrorApiResponse(
                            ApiErrorResponse(
                                "Bad format for token",
                                ApiErrorType.INVALID_CREDENTIALS
                            )
                        )
                    }

                }
            }
        }
    }

    private fun getUserOrganisations(user: User) {
        val headers = RequestHeaders(authorization = user.getBearToken())
        repository.getOrganisations(headers) {
            when (it) {
                is ApiEmptyResponse -> {
                    isLoading.postValue(false)
                    val msg = Pair("No content", ApiErrorType.UNKNOWN_ERROR)
                    onRequestFailed.postValue(msg)
                    Logs.logCat(msg, true)
                }
                is ApiErrorResponse -> with(it) {
                    isLoading.postValue(false)

                    handleUserOrganisationErrorApiResponse(it)
                }
                is ApiSuccessResponse -> {
                    val userOrgs = it.body
                    handleSuccessfulGettingOfUserOrgs(user, userOrgs)
                }
            }
        }
    }

    private fun handleSuccessfulGettingOfUserOrgs(user: User, orgs: List<Organisation>) {
        val credentialsSaved = saveUserProfile(user, orgs)
        if (credentialsSaved) {
            onRequestSuccessful.postValue(user)
        }
        Logs.logCat("user: ${preferences.getUserProfile()}")
        Logs.logCat("Orgs: ${preferences.getUserOrganisations()}")
    }

    private fun saveUserProfile(user: User, orgs: List<Organisation>): Boolean {
        return if (orgs.isEmpty()) {
            Logs.logCat("User has no related organisation")
            false
        } else preferences.saveUserDetails(user, orgs)
    }


    private fun handleUserOrganisationErrorApiResponse(apiResponse: ApiErrorResponse<List<Organisation>>) {
        val msg = when (apiResponse.errorType) {
            ApiErrorType.NOT_AUTHENTICATED -> "Connexion échouée"
            ApiErrorType.ACCESS_DENIED -> "Accès refusé"
            ApiErrorType.PAGE_NOT_FOUND -> "Page non trouvée"
            ApiErrorType.SERVER_ERROR -> "Un problème est survenu sur le serveur"
            ApiErrorType.INVALID_CREDENTIALS -> "Nom d'utilisateur ou mot de passe invalides"
            ApiErrorType.SERVER_NOT_RESPONDING -> "La plateforme ne répond pas"
            ApiErrorType.TIMEOUT -> "Délai expirée. Veuillez réessayer."
            ApiErrorType.UNKNOWN_HOST -> "Adresse non joignable"
            ApiErrorType.UNKNOWN_ERROR -> "Une erreur inconnue est survenue"
        }

        onRequestFailed.postValue(Pair(msg, apiResponse.errorType))
        Logs.logCat("USER ORG ERROR $msg", false)
    }

    private fun handleLoginErrorApiResponse(apiResponse: ApiErrorResponse<User>) {
        val msg = when (apiResponse.errorType) {
            ApiErrorType.NOT_AUTHENTICATED -> "Connexion échouée"
            ApiErrorType.ACCESS_DENIED -> "Accès refusé"
            ApiErrorType.PAGE_NOT_FOUND -> "Page non trouvée"
            ApiErrorType.SERVER_ERROR -> "Un problème est survenu sur le serveur"
            ApiErrorType.INVALID_CREDENTIALS -> "Nom d'utilisateur ou mot de passe invalides"
            ApiErrorType.SERVER_NOT_RESPONDING -> "La plateforme ne répond pas"
            ApiErrorType.TIMEOUT -> "Délai expirée. Veuillez réessayer."
            ApiErrorType.UNKNOWN_HOST -> "Adresse non joignable"
            ApiErrorType.UNKNOWN_ERROR -> "Une erreur inconnue est survenue"
        }

        onRequestFailed.postValue(Pair(msg, apiResponse.errorType))
        Logs.logCat("LOGIN ERROR $msg", false)
    }


}

data class AuthFormData(val email: String, val password: String)