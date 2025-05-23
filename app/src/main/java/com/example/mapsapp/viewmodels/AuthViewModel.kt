package com.example.mapsapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AuthViewModel(private val sharedPreferences: SharedPreferencesHelper) : ViewModel(){
    private val authManager = MyApp.database

    private val _email = MutableLiveData<String>()
    val email = _email
    private val _password = MutableLiveData<String>()
    val password = _password
    private val _authState = MutableLiveData<AuthState>()
    val authState = _authState
    private val _showError = MutableLiveData<Boolean>(false)
    val showError = _showError
    private val _userId = MutableLiveData<String?>()
    val userId = _userId

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val accessToken = sharedPreferences.getAccessToken()
            val refreshToken = sharedPreferences.getRefreshToken()
            when {
                !accessToken.isNullOrEmpty() -> {
                    refreshToken()
                    val session = authManager.retrieveCurrentSession()
                    _userId.value = session?.user?.id
                }
                !refreshToken.isNullOrEmpty() -> {
                    refreshToken()
                    val session = authManager.retrieveCurrentSession()
                    _userId.value = session?.user?.id
                }
                else -> _authState.value = AuthState.Unauthenticated
            }
        }
    }
    fun editEmail(value: String) {
        _email.value = value
    }

    fun editPassword(value: String) {
        _password.value = value
    }
    fun errorMessageShowed(){
        _showError.value = false
    }
    fun signUp() {
        viewModelScope.launch {
            _authState.value = authManager.signUpWithEmail(_email.value!!, _password.value!!)
            if (_authState.value is AuthState.Error) {
                _showError.value = true

            } else {
                val session = authManager.retrieveCurrentSession()
                _userId.value = session?.user?.id

                Log.d("AuthViewModel", "Access Token: ${session}")
                sharedPreferences.saveAuthData(
                    session!!.accessToken,
                    session.refreshToken
                )
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _authState.value = authManager.signInWithEmail(_email.value!!, _password.value!!)
            if (_authState.value is AuthState.Error) {
                _showError.value = true
                Log.d("AuthViewModel", "Error: ${(_authState.value as AuthState.Error).message}")
            } else {
                val session = authManager.retrieveCurrentSession()
                _userId.value = session?.user?.id

                sharedPreferences.saveAuthData(
                    session!!.accessToken,
                    session.refreshToken
                )
                Log.d("AuthViewModel", "Access Token: ${session.accessToken}")
            }
        }
    }
    private fun refreshToken() {
        viewModelScope.launch {
            try {
                authManager.refreshSession()
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                sharedPreferences.clear()
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    fun logout(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // Cierra sesión en Supabase
            authManager.client.auth.signOut()
            // Limpia datos locales
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().clear().apply()
            // Navega a login (en UI thread)
            withContext(Dispatchers.Main) {
                // Lógica de navegación a LoginActivity o pantalla de login
            }
        }
    }

}
