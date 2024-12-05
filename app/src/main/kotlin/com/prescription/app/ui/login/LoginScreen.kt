package com.prescription.app.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prescription.app.R
import com.prescription.core.design.theme.PrescriptionTheme
import com.prescription.app.ui.destinations.NavigationScreenDestination
import com.prescription.database.entities.UserCredentialEntity
import com.prescription.database.repository.UserCredentialRepository
import com.prescription.utils.navigation.FadeInOutAnimation
import com.prescription.utils.navigation.MockDestinationsNavigator
import com.prescription.utils.preview.UIModePreviews
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk

@Destination(style = FadeInOutAnimation::class)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val isFormValid = username.isNotEmpty() && password.isNotEmpty()
    val loginState = viewModel.loginState.value

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(96.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = username.isEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (isFormValid) {
                            viewModel.login(username, password)
                        }
                    }),
                    isError = password.isEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { /* navigate to forgot password */ }) {
                    Text(
                        text = "Forgot Password?",
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (isFormValid) {
                            viewModel.login(username, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid
                ) {
                    Text("Login", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (loginState) {
                    is LoginState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is LoginState.Error -> {
                        Text(
                            text = loginState.errorMessage,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.body2
                        )
                    }
                    is LoginState.Success -> {
                        Text(
                            text = loginState.message,
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.primary
                        )
                        LaunchedEffect(Unit) {
                            navigator.navigate(NavigationScreenDestination)
                        }
                    }
                    else -> {}
                }
            }
        }
    )
}

@Composable
fun provideMockLoginViewModel(): LoginViewModel {
    val mockRepository = mockk<UserCredentialRepository>()

    coEvery { mockRepository.saveCredential(any(), any()) } just Runs
    coEvery { mockRepository.getCredentialByUsername(any()) } returns UserCredentialEntity("user", "password")

    val mockState = remember { mutableStateOf<LoginState>(LoginState.Success("Welcome!")) }
    return LoginViewModel(mockRepository).apply {
        loginState = mockState
    }
}


@UIModePreviews
@Composable
private fun LoginScreenPreview() {
    PrescriptionTheme {
        val mockViewModel = provideMockLoginViewModel()
        LoginScreen(
            navigator = MockDestinationsNavigator(),
            viewModel = mockViewModel
        )
    }
}


