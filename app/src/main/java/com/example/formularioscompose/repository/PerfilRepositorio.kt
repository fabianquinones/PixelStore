package com.example.formularioscompose.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

class PerfilRepositorio(context: Context) {

    private val prefs = UsuarioPreferences(context)

    suspend fun guardarDatos(nombre: String, correo: String, clave: String, direccion: String) {
        val usuario = Usuario(nombre, correo, clave, direccion)
        prefs.guardarUsuario(usuario)
    }

    fun obtenerDatos(): Flow<List<Usuario>> = prefs.obtenerUsuarios

    suspend fun limpiar() {
        prefs.limpiarDatos()
    }
}
