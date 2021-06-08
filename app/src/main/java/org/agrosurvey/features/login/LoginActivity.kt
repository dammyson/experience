package org.agrosurvey.features.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.agrosurvey.R
import com.agrosurvey.databinding.ActivityLoginBinding
import com.agrosurvey.survey.MySurvey
import com.fabricethilaw.sonarnet.InternetStatus.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.agrosurvey.Constants
import org.agrosurvey.PreferenceHelper
import org.agrosurvey.executeWithInternet
import org.agrosurvey.features.survey.MainActivity
import org.agrosurvey.validators.LoginValidators
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity @Inject constructor() : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var binding: ActivityLoginBinding
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            fieldUsername.editText?.doOnTextChanged { text, _, _, _ ->
                labelLoginError.isVisible = false
                if (!text.isNullOrEmpty() && !fieldUsername.error.isNullOrBlank()) {
                    fieldUsername.error = null
                }
            }

            fieldPassword.editText?.doOnTextChanged { text, _, _, _ ->
                labelLoginError.isVisible = false
                if (!text.isNullOrEmpty() && !fieldPassword.error.isNullOrBlank()) {
                    fieldPassword.error = null
                }
            }

            handleLoginRequestEvents()
            button_login.setOnClickListener {
                onClickToSignIn()
            }
        }
        supportActionBar?.hide()
    }

    private fun ActivityLoginBinding.onClickToSignIn() {

        if (viewModel.isLoading.value == false) {
            validateForm {
                executeWithInternet(root) { viewModel.login(it) }
            }
        }
    }


    private fun ActivityLoginBinding.validateForm(callback: (AuthFormData) -> Unit) {
        var username: String
        var password: String
        fieldUsername.apply {
            username = editText?.text?.toString().orEmpty().trim()
            if (LoginValidators.isValidEmail(username).not()) {
                error = getString(R.string.error_required_field)
            }
        }

        fieldPassword.apply {
            password = editText?.text?.toString().orEmpty().trim()
            if (LoginValidators.isValidPassword(password).not()) {
                error = getString(R.string.error_required_field)
            }
        }

        LoginValidators.textFieldsToFormData(
            email = username, password = password
        )?.let { callback(it) }

    }

    private fun ActivityLoginBinding.handleLoginRequestEvents() {
        viewModel.isLoading.observe(this@LoginActivity) { isLoading ->
            lockFields(isLoading)
        }


        viewModel.onRequestFailed.observe(this@LoginActivity) {
            labelLoginError.isVisible = true
            labelLoginError.text = it.first
        }

        viewModel.onRequestSuccessful.observe(this@LoginActivity) {
            goToMainScreen()
        }
    }

    private fun ActivityLoginBinding.lockFields(isLoading: Boolean) {
        fieldUsername.isEnabled = !isLoading
        fieldPassword.isEnabled = !isLoading
        labelForgotPassword.isEnabled = !isLoading
        otherProducts.isEnabled = !isLoading
        labelLoginError.isVisible = isLoading

        buttonLogin.text = getString(if (isLoading) R.string.please_wait else R.string.login)
    }

    private fun goToMainScreen() {
        val token: String = preferenceHelper.getToken();
        if (!token.isNullOrEmpty()) {
            val mySurvey = MySurvey.Builder(this)
                .setUserToken(token)
                .setBaseUrl(Constants.API_URL)
                .build()

            GlobalScope.launch(CoroutineExceptionHandler { _, e -> Log.e("RETRIEVE",e.stackTraceToString())}) {
                mySurvey.fetchAndSaveSurveys()
                mySurvey.fetchQuestionsType()
            }
        }
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }



    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this@LoginActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@LoginActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.e("myTag", "Love one")
                ActivityCompat.requestPermissions(this@LoginActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

            } else {

                ActivityCompat.requestPermissions(this@LoginActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }else{

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@LoginActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {

                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


}