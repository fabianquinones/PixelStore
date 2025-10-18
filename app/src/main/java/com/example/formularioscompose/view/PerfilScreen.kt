package com.example.formularioscompose.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.formularioscompose.view.components.ImagenInteligente
import com.example.formularioscompose.viewmodel.PerfilViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PerfilScreen(viewModel: PerfilViewModel = viewModel()) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.setImage(uri) }

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) viewModel.setImage(cameraUri) }

    fun createImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${'$'}timestamp_", ".jpg", storageDir)
        return FileProvider.getUriForFile(context, "${'$'}{context.packageName}.fileprovider", file)
    }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            cameraUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImagenInteligente(imagenUri)
        Spacer(Modifier.height(24.dp))

        Button(onClick = { pickImageLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text("Seleccionar desde galería")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                    val uri = createImageUri(context)
                    cameraUri = uri
                    takePictureLauncher.launch(uri)
                }
                else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Tomar foto con cámara")
        }
    }
}
