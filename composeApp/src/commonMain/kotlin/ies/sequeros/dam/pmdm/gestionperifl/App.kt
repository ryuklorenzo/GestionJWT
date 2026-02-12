package ies.sequeros.dam.pmdm.gestionperifl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource

import gestionjwt.composeapp.generated.resources.Res
import gestionjwt.composeapp.generated.resources.compose_multiplatform
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginState
import ies.sequeros.dam.pmdm.gestionperifl.ui.login.LoginScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.principal.MainScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterScreen
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object HomeRoute
@Serializable
object RegisterRoute


@Composable
@Preview
fun App() {
    val appViewModel: AppViewModel = koinViewModel()
    val navController = rememberNavController()

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
                startDestination = LoginRoute
            ) {
                composable<LoginRoute> {
                    LoginScreen(
                        onLogin = {
                            navController.navigate(HomeRoute) {
                                popUpTo(HomeRoute) { inclusive = true }
                            }
                        },
                        onRegister = {
                            navController.navigate(RegisterRoute)
                        },
                        onCancel = {
                        }
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
                    MainScreen()
                }
            }
        }
    }
}