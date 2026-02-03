package ies.sequeros.dam.pmdm.gestionperifl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    val AndroidPlatformModule = module {
        single<Settings> {
            // Obtenemos el archivo de SharedPreferences usando el contexto de Android
            val sharedPrefs = androidContext().getSharedPreferences(
                "mi_app_prefs",
                android.content.Context.MODE_PRIVATE
            )
            // Aquí instanciarías tu implementación de Android
            AndroidSettings(sharedPrefs)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MyApplication)
            androidLogger() // Opcional: para ver logs de Koin
            modules(appModule, AndroidPlatformModule) // Tu módulo de dependencias
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}