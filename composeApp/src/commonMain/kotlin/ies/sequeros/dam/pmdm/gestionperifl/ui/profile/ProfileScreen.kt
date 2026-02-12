import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun ProfileScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Pantalla de Perfil", style = MaterialTheme.typography.titleLarge)
        // Aquí iría tu componente de visualización de perfil
    }
}