package ies.sequeros.dam.pmdm.gestionperifl.ui.imagen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChangeImageScreen(
    viewModel: ChangeImageViewModel,
    userId: String,
    onImageChanged: () -> Unit
){
    val state by viewModel.state.collectAsState()

    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onImageChanged()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cambiar imagen",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text(text = "Image url") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            viewModel.changeImage(userId, imageUrl)
        },
            enabled = !state.isLoading && imageUrl.isNotBlank()
        ) {

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Text("Guardar")
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.errorMessage != null) {
            Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

    }
}