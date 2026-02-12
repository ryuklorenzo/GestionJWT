package ies.sequeros.dam.pmdm.gestionperifl.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.dam.pmdm.gestionperifl.ProfileRoute
import ies.sequeros.dam.pmdm.gestionperifl.EditProfileRoute
import ies.sequeros.dam.pmdm.gestionperifl.PasswordRoute
//import ies.sequeros.dam.pmdm.gestionperifl.ui.profile.ProfileScreen
//import ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile.EditProfileScreen
//import ies.sequeros.dam.pmdm.gestionperifl.ui.password.ChangePasswordScreen


data class MenuOption(
    val icon: ImageVector,
    val name: String,
    val action: () -> Unit
)

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val subNavController = rememberNavController()

    // Configuración de las opciones del menú (Similar a ItemOption en VegaBurguer)
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
            // Este es el panel derecho que cambia dinámicamente
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavHost(
                    navController = subNavController,
                    startDestination = ProfileRoute
                ) {
                    composable<ProfileRoute> {
                        //ProfileScreen()
                    }
                    composable<EditProfileRoute> {
                        //EditProfileScreen()
                    }
                    composable<PasswordRoute> {
                        //ChangePasswordScreen()
                    }
                }
            }
        }
    )
}