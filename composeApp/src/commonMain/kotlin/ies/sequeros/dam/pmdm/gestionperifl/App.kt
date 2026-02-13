package ies.sequeros.dam.pmdm.gestionperifl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.dam.pmdm.gestionperifl.application.session.SessionManager
import ies.sequeros.dam.pmdm.gestionperifl.ui.HomeScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.login.LoginScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject // Necesario para inyectar SessionManager
import kotlinx.serialization.Serializable

// Rutas Principales
@Serializable
object LoginRoute
@Serializable
object RegisterRoute
@Serializable
object HomeRoute

// Rutas Internas del Home
@Serializable
object ProfileRoute
@Serializable
object EditProfileRoute
@Serializable
object PasswordRoute
@Serializable
object ImageRoute


@Composable
fun App() {
    val appViewModel: AppViewModel = koinViewModel()

    // Inyectamos SessionManager para comprobar el login
    val sessionManager: SessionManager = koinInject()

    val navController = rememberNavController()

    //si el usuario está logueado, vamos directos a HomeRoute. Si no, a LoginRoute.
    val startDestination: Any = if (sessionManager.isLoggedIn()) {
        HomeRoute
    } else {
        LoginRoute
    }

    AppTheme(appViewModel.isDarkMode.collectAsState()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable<LoginRoute> {
                    LoginScreen(
                        onLogin = {
                            navController.navigate(HomeRoute) {
                                popUpTo(LoginRoute) { inclusive = true }
                            }
                        },
                        onRegister = {
                            navController.navigate(RegisterRoute)
                        },
                        onCancel = {}
                    )
                }

                composable<RegisterRoute> {
                    RegisterScreen(
                        onRegisterSuccess = {
                            navController.navigate(LoginRoute) {
                                popUpTo(RegisterRoute) { inclusive = true }
                            }
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<HomeRoute> {
                    HomeScreen(
                        onLogout = {
                            //Con el logout limpiamos token
                            sessionManager.clearSession()
                            navController.navigate(LoginRoute) {
                                popUpTo(HomeRoute) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}