package ies.sequeros.dam.pmdm.gestionperifl.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    // Inyectamos el RegisterFormViewModel
    val viewModel = koinViewModel<RegisterFormViewModel>()
    val state by viewModel.state.collectAsState()

    // Efecto: Si el registro es exitoso, navegamos
    LaunchedEffect(state.isRegisterSuccess) {
        if (state.isRegisterSuccess) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrarse") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Campo Nombre
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.nameError != null,
                    supportingText = { state.nameError?.let { Text(it) } },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.emailError != null,
                    supportingText = { state.emailError?.let { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Password
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.passwordError != null,
                    supportingText = { state.passwordError?.let { Text(it) } },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Mensaje de error general (si falla el servidor)
                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Registrarse
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { viewModel.onSubmit() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Crear cuenta")
                    }
                }
            }
        }
    }
}