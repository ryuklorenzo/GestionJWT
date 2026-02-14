package ies.sequeros.dam.pmdm.gestionperifl.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Perfil",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.errorMessage != null -> {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                ProfileItem("Usuario:", state.name)
                Spacer(modifier = Modifier.height(20.dp))
                ProfileItem("Email:", state.email)
                Spacer(modifier = Modifier.height(20.dp))
                ProfileItem("Imagenes:", state.image, isImage = true)
                Spacer(modifier = Modifier.height(20.dp))
                ProfileItem("Estado de la cuenta:", state.status)
            }
        }
    }
}