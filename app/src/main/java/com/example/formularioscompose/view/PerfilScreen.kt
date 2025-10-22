package com.example.formularioscompose.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.formularioscompose.view.components.ImagenInteligente
import com.example.formularioscompose.viewmodel.PerfilViewModel
import com.example.formularioscompose.viewmodel.UsuarioViewModel // <-- 1. IMPORTAMOS EL VIEWMODEL DEL USUARIO

@Composable
fun PerfilScreen(
    // 2. AÑADIMOS LOS DOS VIEWMODELS COMO PARÁMETROS
    perfilViewModel: PerfilViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel()
) {
    val context = LocalContext.current
    // Obtenemos el estado de la imagen desde PerfilViewModel
    val imagenUri by perfilViewModel.imagenUri.collectAsState()
    // Obtenemos el estado de los datos desde UsuarioViewModel
    val estadoUsuario by usuarioViewModel.estado.collectAsStateWithLifecycle()

    // --- Launchers para la cámara y galería (sin cambios) ---
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            perfilViewModel.setImage(uri)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            perfilViewModel.guardarImagenDeCamara()
        }
    }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = perfilViewModel.crearUriParaCamara(context)
            takePictureLauncher.launch(uri)
        }
    }

    // --- Interfaz de usuario ---
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostramos la imagen del perfil
            ImagenInteligente(imagenUri)
            Spacer(Modifier.height(24.dp))

            // 3. MOSTRAMOS LOS DATOS DEL USUARIO LOGUEADO
            Text(
                text = estadoUsuario.nombre,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = estadoUsuario.correo,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = estadoUsuario.direccion,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp)) // Espacio más grande antes de los botones

            // Botones para cambiar la foto
            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar desde galería")
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    val permission = Manifest.permission.CAMERA
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        val uri = perfilViewModel.crearUriParaCamara(context)
                        takePictureLauncher.launch(uri)
                    } else {
                        requestCameraPermission.launch(permission)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto con cámara")
            }
        }
    }
}
