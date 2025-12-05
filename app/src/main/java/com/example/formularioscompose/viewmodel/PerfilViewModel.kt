// En tu archivo PerfilViewModel.kt

package com.example.formularioscompose.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PerfilViewModel : ViewModel() {
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri = _imagenUri.asStateFlow()

    // Variable para guardar la URI de la cámara temporalmente
    private var _cameraUri: Uri? = null

    fun setImage(uri: Uri?) {
        _imagenUri.value = uri
    }
    // --- NUEVA FUNCIÓN ---
    // Esta función crea una URI, la guarda internamente y la devuelve.
    // La pantalla la llamará para obtener la URI antes de lanzar la cámara.
    fun crearUriParaCamara(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        _cameraUri = uri // Guardamos la URI creada
        return uri
    }

    // --- NUEVA FUNCIÓN ---
    // Esta función se llamará cuando la cámara confirme que la foto se guardó.
    // Usará la URI que guardamos previamente.
    fun guardarImagenDeCamara() {
        _imagenUri.value = _cameraUri
    }
}
