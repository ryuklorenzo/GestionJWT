package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditProfileScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Editar Datos de Usuario", style = MaterialTheme.typography.titleLarge)
        // Aquí iría tu formulario de edición
        Spacer(Modifier.height(40.dp))
        Column {
            Text("PUES VAMOS A EDITAR COSITAS NO?")
        }
    }
}