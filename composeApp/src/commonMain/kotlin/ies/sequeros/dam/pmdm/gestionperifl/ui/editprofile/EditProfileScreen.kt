package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.dam.pmdm.gestionperifl.domain.model.UserStatus
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.form.ComboBox

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

        ComboBox(
            items = UserStatus.values().toList(),
            label = "Estado",
            current = state.status,
            itemLabel = {
                when (it) {
                    UserStatus.ACTIVE -> "Activo"
                    UserStatus.INACTIVE -> "Inactivo"
                    UserStatus.BANNED -> "Bloqueado"
                }
            },
            onSelect = { viewModel.onStatusChange(it) },
            editable = true
        )

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
            Text(state.errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

    }

}