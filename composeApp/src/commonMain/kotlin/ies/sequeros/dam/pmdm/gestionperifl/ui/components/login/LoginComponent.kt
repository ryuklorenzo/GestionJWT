package ies.sequeros.dam.pmdm.gestionperifl.ui.components.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginComponent(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo Email
        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.emailError != null,
            supportingText = {
                if (state.emailError != null) {
                    Text(state.emailError)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = state.passwordError != null,
            supportingText = {
                if (state.passwordError != null) {
                    Text(state.passwordError)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Botones
        if (state.isLoading) {
            // Si está cargando, mostramos la rueda
            CircularProgressIndicator()
        } else {
            // Si no, mostramos los botones (Entrar, Registrarse, Cancelar)

            // Botón Entrar
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isValid // Se deshabilita si el formato email/pass no es válido
            ) {
                Text("Entrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Registrarse
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Cancelar
            TextButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}