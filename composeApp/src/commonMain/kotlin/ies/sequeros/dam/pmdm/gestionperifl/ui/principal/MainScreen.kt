package ies.sequeros.dam.pmdm.gestionperifl.ui.principal

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(

){
    val viewModel = koinViewModel<MainFormVIewModel>()
    //val state by viewModel.state.collectAsState()
    Text("Bienvenido al Main")
}