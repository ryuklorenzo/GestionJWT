package ies.sequeros.dam.pmdm.gestionperifl.ui.principal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.dam.pmdm.gestionperifl.DeleteRoute
import ies.sequeros.dam.pmdm.gestionperifl.ProfileRoute
import ies.sequeros.dam.pmdm.gestionperifl.EditProfileRoute
import ies.sequeros.dam.pmdm.gestionperifl.ImageRoute
import ies.sequeros.dam.pmdm.gestionperifl.PasswordRoute
import ies.sequeros.dam.pmdm.gestionperifl.ui.deleteaccount.DeleteAccountScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.deleteaccount.DeleteAccountViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile.EditProfileScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.imagen.ChangeImageScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.password.ChangePasswordScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.password.ChangePasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

// Nuevos imports necesarios para la lógica del ID de usuario
import org.koin.compose.koinInject
import ies.sequeros.dam.pmdm.gestionperifl.application.session.SessionManager
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.TokenJwt
import ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile.EditProfileViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.imagen.ChangeImageViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.profile.ProfileScreen
import ies.sequeros.dam.pmdm.gestionperifl.ui.profile.ProfileViewModel

data class MenuOption(
    val icon: ImageVector,
    val name: String,
    val action: () -> Unit
)

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val subNavController = rememberNavController()

    val changepasswordviewmodel : ChangePasswordViewModel = koinViewModel()
    val deleteacountviewmodel : DeleteAccountViewModel = koinViewModel()
    val profileviewmodel : ProfileViewModel = koinViewModel()
    val editprofileviewmodel : EditProfileViewModel = koinViewModel()
    val changeimageviewmodel : ChangeImageViewModel = koinViewModel()

    val sessionManager: SessionManager = koinInject()
    val userId = remember {
        try {
            sessionManager.getAccessToken()?.let { token ->
                TokenJwt(token).payload.userId
            } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val options = listOf(
        MenuOption(Icons.Default.Person, "Ver Perfil") {
            subNavController.navigate(ProfileRoute) { launchSingleTop = true }
        },
        MenuOption(Icons.Default.Edit, "Editar Perfil") {
            subNavController.navigate(EditProfileRoute) { launchSingleTop = true }
        },
        MenuOption(Icons.Default.Lock, "Seguridad") {
            subNavController.navigate(PasswordRoute) { launchSingleTop = true }
        },
        MenuOption(Icons.Default.Image, "Cambiar Imagen"){
            subNavController.navigate(ImageRoute) { launchSingleTop = true }
        },
        MenuOption(Icons.Default.Delete, "Eliminar Cuenta"){
            subNavController.navigate(DeleteRoute)
        },
        MenuOption(Icons.Default.ExitToApp, "Cerrar Sesión") {
            onLogout()
        }
    )

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(Modifier.width(200.dp)) {
                Column(
                    modifier = Modifier.fillMaxHeight().padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Mi Cuenta",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    options.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = item.name) },
                            label = { Text(item.name) },
                            selected = false,
                            onClick = { item.action() },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavHost(
                    navController = subNavController,
                    startDestination = ProfileRoute
                ) {
                    composable<ProfileRoute> {
                        ProfileScreen(
                            profileviewmodel
                        )
                    }
                    composable<EditProfileRoute> {
                        EditProfileScreen(
                            viewModel { editprofileviewmodel },
                            onProfileUpdate = {
                                profileviewmodel.loadProfile()
                                subNavController.popBackStack()
                            }
                        )
                    }
                    composable<PasswordRoute> {
                        ChangePasswordScreen(
                            changepasswordviewmodel,
                            { onLogout() }
                        )
                    }
                    composable<ImageRoute> {
                        ChangeImageScreen(
                            viewModel = changeimageviewmodel,
                            userId = userId as String,
                            currentImageUrl = profileviewmodel.state.value.image,
                            onImageChanged = {
                                onLogout()
                            }
                        )
                    }
                    composable<DeleteRoute>{
                        DeleteAccountScreen(
                            viewModel = deleteacountviewmodel,
                            userId = userId as String,
                            onAccountDelete = {
                                onLogout()
                            }
                        )
                    }
                }
            }
        }
    )
}