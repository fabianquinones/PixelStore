package com.example.formularioscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.view.CarritoScreen
import com.example.formularioscompose.viewmodel.UsuarioViewModel
import com.example.formularioscompose.view.theme.FormulariosComposeTheme
import com.example.formularioscompose.view.FormularioScreen
import com.example.formularioscompose.view.PerfilScreen
import com.example.formularioscompose.view.RegistroExitosoScreen
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

    // Inicializamos el ViewModel
    val context = LocalContext.current
    val usuarioViewModel = remember { UsuarioViewModel(PerfilRepositorio(context)) }

    // Definimos las rutas de navegación
    NavHost(navController = navController, startDestination = "InicioSesion") {

        composable("InicioSesion") {
            InicioSesion(navController = navController)
        }

        composable("ProductScreen") {
            ProductScreen(navController)
        }

        composable("FormularioScreen") {
            FormularioScreen(navController, usuarioViewModel)
        }

        composable("RegistroExitosoScreen") {
            RegistroExitosoScreen(navController)
        }

        composable("perfil") {
            PerfilScreen()
        }
        composable("carrito") {
            CarritoScreen(navController = navController)
        }
    }
}
