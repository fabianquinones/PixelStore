package com.example.formularioscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.formularioscompose.view.theme.FormulariosComposeTheme
import com.example.formularioscompose.view.FormularioScreen
import com.example.formularioscompose.view.PerfilScreen
import com.example.formularioscompose.view.ResumenScreen
import com.example.formularioscompose.viewmodel.UsuarioViewModel
import com.example.formularioscompose.view.InicioSesion
import com.example.formularioscompose.view.ProductScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormulariosComposeTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "InicioSesion") {
        composable("InicioSesion") { InicioSesion(navController = navController) }
        composable("ProductScreen") { ProductScreen(navController) }
        composable("FormularioScreen") { FormularioScreen(navController, usuarioViewModel) }
        composable("resumen") { ResumenScreen(usuarioViewModel) }
        composable("perfil") { PerfilScreen() }
    }
}


