package com.example.formularioscompose.repository

import android.net.Uri
import com.example.formularioscompose.model.PerfilDeUsuario

class PerfilRepositorio {
    private var perfilActual = PerfilDeUsuario(1, "Usuario", null)

    fun getProfile(): PerfilDeUsuario = perfilActual

    fun updateImage(uri: Uri?) {
        perfilActual = perfilActual.copy(imagenUri = uri)
    }
}
