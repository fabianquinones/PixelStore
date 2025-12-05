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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.view.components.ImagenInteligente
import com.example.formularioscompose.viewmodel.PerfilViewModel
import com.example.formularioscompose.viewmodel.UsuarioViewModel
import com.example.formularioscompose.viewmodel.FraseViewModel

@Composable
fun PerfilScreen() {
    val context = LocalContext.current

    val usuarioViewModel = remember { UsuarioViewModel(PerfilRepositorio(context)) }
    val perfilViewModel = remember { PerfilViewModel() }
    val fraseViewModel: FraseViewModel = viewModel()

    // Estados
    val estadoUsuario by usuarioViewModel.estado.collectAsStateWithLifecycle()
    val imagenUri by perfilViewModel.imagenUri.collectAsState()
    val frase by fraseViewModel.frase.collectAsStateWithLifecycle()
    val fraseError by fraseViewModel.error.collectAsStateWithLifecycle()

    // Cargar usuario al entrar
    LaunchedEffect(Unit) {
        usuarioViewModel.cargarUsuario()
    }


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
            // FOTO / AVATAR
            ImagenInteligente(imagenUri)
            Spacer(Modifier.height(24.dp))

            // DATOS DEL USUARIO
            if (estadoUsuario.nombre.isNotBlank()) {
                Text(
                    text = estadoUsuario.nombre + " " + estadoUsuario.apellido,
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
                    text = "+56"+" "+estadoUsuario.telefono,
                    style = MaterialTheme.typography.bodyMedium,
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


            Spacer(Modifier.height(24.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            // 🔹 API EXTERNA: FRASE DEL DÍA
            Text(
                text = "Frase del día (API externa)",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            when {
                fraseError != null -> {
                    Text(
                        text = fraseError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { fraseViewModel.cargarFrase() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reintentar")
                    }
                }
                frase != null -> {
                    Text(
                        text = "\"${frase!!.content}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "- ${frase!!.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> {
                    Text(
                        text = "Cargando frase...",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // BOTONES DE IMAGEN
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
