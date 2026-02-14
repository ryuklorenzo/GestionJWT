package ies.sequeros.dam.pmdm.gestionperifl.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ies.sequeros.dam.pmdm.gestionperifl.SERVER_PORT

@Composable
fun ProfileItem(label: String, value: String, isImage: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)

        if (isImage && value.isNotEmpty()) {
            val imageUrl = "http://127.0.0.1:8080/uploads/users/$value"

            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen de usuario",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}