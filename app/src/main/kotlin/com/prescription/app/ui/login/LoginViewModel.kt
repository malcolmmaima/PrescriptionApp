package com.prescription.app.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prescription.database.repository.UserCredentialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
) : ViewModel() {

    open var loginState = mutableStateOf<LoginState>(LoginState.Idle)

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    open fun login(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            loginState.value = LoginState.Loading

            viewModelScope.launch {
                delay(2000)

                val storedCredential = userCredentialRepository.getCredentialByUsername(username)

                if (storedCredential != null) {
                    if (BCrypt.checkpw(password, storedCredential.hashedPassword)) {
                        loginState.value = LoginState.Success("Welcome back, $username!")
                    } else {
                        loginState.value = LoginState.Error("Invalid credentials, logging out current user!")
                        purgeUsersTable()
                    }
                } else {
                    saveCredentials(username, password)
                    loginState.value = LoginState.Success("Login successful, welcome $username!")
                }
            }
        } else {
            loginState.value = LoginState.Error("Please fill in all fields")
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val hashedPassword = hashPassword(password)
        viewModelScope.launch(Dispatchers.IO) {
            userCredentialRepository.saveCredential(username, hashedPassword)
        }
    }

    private fun purgeUsersTable(){
        viewModelScope.launch(Dispatchers.IO) {
            userCredentialRepository.deleteUsersTable()
            delay(1000)
            withContext(Dispatchers.Main) {
                loginState.value = LoginState.Error("Done, login again!")
            }
        }
    }
}


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val errorMessage: String) : LoginState()
}

