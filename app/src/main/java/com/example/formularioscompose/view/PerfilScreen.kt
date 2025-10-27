package com.example.formularioscompose.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.view.components.ImagenInteligente
import com.example.formularioscompose.viewmodel.PerfilViewModel
import com.example.formularioscompose.viewmodel.UsuarioViewModel

@Composable
fun PerfilScreen() {
    val context = LocalContext.current

    val usuarioViewModel = remember { UsuarioViewModel(PerfilRepositorio(context)) }
    val perfilViewModel = remember { PerfilViewModel() }


    LaunchedEffect(Unit) {
        usuarioViewModel.cargarUsuario()
    }

    val imagenUri by perfilViewModel.imagenUri.collectAsState()
    val estadoUsuario by usuarioViewModel.estado.collectAsStateWithLifecycle()


    // Launchers
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) perfilViewModel.setImage(uri)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) perfilViewModel.guardarImagenDeCamara()
    }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = perfilViewModel.crearUriParaCamara(context)
            takePictureLauncher.launch(uri)
        }
    }

    // UI
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenInteligente(imagenUri)
            Spacer(Modifier.height(24.dp))

            // ✅ Mostrar datos del usuario (control de null)
            if (estadoUsuario.nombre.isNotBlank()) {
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
            } else {
                Text(
                    text = "No hay usuario cargado",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(32.dp))

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
                    if (ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
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
