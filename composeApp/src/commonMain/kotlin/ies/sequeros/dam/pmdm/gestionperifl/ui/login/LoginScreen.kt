package ies.sequeros.dam.pmdm.gestionperifl.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLogin: () -> Unit,      // Callback para navegar al Home
    onRegister: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel = koinViewModel<LoginFormViewModel>()
    val state by viewModel.state.collectAsState()

    // Efecto secundario: Navegación
    // Escucha cambios en state.isLoginSuccess. Si se pone a true, navega.
    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) {
            onLogin() // Navega a HomeScreen
        }
    }

    LoginComponent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = {
            viewModel.login()
        },
        onRegisterClick = {
            onRegister()
        },
        onCancel = {
            onCancel()
        }
    )
}