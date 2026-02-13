package ies.sequeros.dam.pmdm.gestionperifl.ui.password
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
fun ChangePasswordScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cambiar Contraseña", style = MaterialTheme.typography.titleLarge)
        // Aquí iría tu formulario de cambio de clave

        Spacer(Modifier.height(40.dp))
        Column {
            Text("VOY A CAMBIAR LA CONTRASEÑA POR SEGURIDAD")
            Text("NO PORQUE SE ME HAYA OLVIDADO VALE?")
        }
    }
}