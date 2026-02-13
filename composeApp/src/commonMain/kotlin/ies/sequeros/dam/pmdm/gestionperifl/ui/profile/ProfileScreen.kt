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
fun ProfileScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Pantalla de Perfil", style = MaterialTheme.typography.titleLarge)
        // Aquí iría tu componente de visualización de perfil
        Spacer(Modifier.height(40.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("PUES AQUI IRIA EL PERFIL CHACHO")
        }
    }

}