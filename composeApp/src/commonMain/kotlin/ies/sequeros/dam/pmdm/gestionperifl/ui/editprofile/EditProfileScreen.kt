package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun EditProfileScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Editar Datos de Usuario", style = MaterialTheme.typography.titleLarge)
        // Aquí iría tu formulario de edición
    }
}