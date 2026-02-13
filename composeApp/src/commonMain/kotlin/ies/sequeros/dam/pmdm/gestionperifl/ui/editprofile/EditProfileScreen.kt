package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onProfileUpdate: () -> Unit,
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onProfileUpdate()
        }
    }

    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Editar Datos de Usuario", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = {viewModel.onUsernameChange(it)},
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.usernameError != null
        )
        state.usernameError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.emailError != null
        )
        state.emailError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.updateProfile() },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isValid && !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Guardar cambios")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.errorMessage != null) {
            Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
        }

    }

}