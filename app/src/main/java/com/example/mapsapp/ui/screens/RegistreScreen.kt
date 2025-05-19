package com.example.mapsapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory

@Composable
fun RegisterScreen(
    navigatetopermision: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))
    val authState by viewModel.authState.observeAsState()
    val showError = viewModel.showError.observeAsState(false)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (authState == AuthState.Authenticated) {
        navigatetopermision()
    } else {
        if (showError.value) {
            val errorMessage = (authState as? AuthState.Error)?.message.orEmpty()
            val toastMessage = when {
                errorMessage.contains("weak_password", ignoreCase = true) -> "La contraseña debe tener al menos 6 caracteres."
                errorMessage.contains("invalid_email", ignoreCase = true) -> "Correo electrónico inválido."
                errorMessage.contains("email_already", ignoreCase = true) -> "Ya existe una cuenta con ese correo."
                else -> "Ocurrió un error al crear la cuenta."
            }

            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            viewModel.errorMessageShowed()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.editEmail(it)
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.editPassword(it)
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signUp() },
                enabled = email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Crear cuenta")
            }
        }
    }
}
