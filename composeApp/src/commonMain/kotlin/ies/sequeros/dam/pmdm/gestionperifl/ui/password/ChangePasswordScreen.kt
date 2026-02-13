package ies.sequeros.dam.pmdm.gestionperifl.ui.password

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChangePasswordScreen(viewModel: ChangePasswordViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Cambiar Contraseña", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.oldPassword,
            onValueChange = { viewModel.onOldPasswordChange(it) },
            label = { Text("Contraseña antigua") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.oldPasswordError != null
        )

        state.oldPasswordError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.newPassword,
            onValueChange = { viewModel.onNewPasswordChange(it) },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.newPasswordError != null
        )
        state.newPasswordError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.changePassword() },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isValid && !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Cambiar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isSuccess) {
            Text(
                "Contraseña cambiada con éxito",
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            state.errorMessage?.let { msg ->
                Text(
                    msg,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
