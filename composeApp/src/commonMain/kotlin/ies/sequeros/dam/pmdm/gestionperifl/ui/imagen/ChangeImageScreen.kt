package ies.sequeros.dam.pmdm.gestionperifl.ui.imagen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.dam.pmdm.gestionperifl.ui.components.ImagePickerPreviewComponent
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes

@Composable
fun ChangeImageScreen(
    viewModel: ChangeImageViewModel,
    userId: String,
    currentImageUrl: String?,
    onImageChanged: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var selectedFile by remember { mutableStateOf<PlatformFile?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ImagePickerPreviewComponent(
            imageUrl = currentImageUrl,
            selectedFile = selectedFile,
            onFileSelected = { file ->
                selectedFile = file
            },
            onConfirm = {
                selectedFile?.let { file ->
                    val bytes = file.readBytes()
                    viewModel.changeImage(userId, bytes)
                }
            }
        )

        LaunchedEffect(state.isSuccess) {
            if (state.isSuccess) {
                onImageChanged()
            }
        }
    }
}
