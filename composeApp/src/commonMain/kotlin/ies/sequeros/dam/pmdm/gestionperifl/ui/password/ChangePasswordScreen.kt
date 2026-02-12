package ies.sequeros.dam.pmdm.gestionperifl.ui.password
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
@Composable
fun ChangePasswordScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cambiar Contraseña", style = MaterialTheme.typography.titleLarge)
        // Aquí iría tu formulario de cambio de clave
    }
}