package com.example.formularioscompose.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

class PerfilRepositorio(context: Context) {

    private val prefs = UsuarioPreferences(context)

    // --- Guarda los datos del usuario en DataStore ---
    suspend fun guardarDatos(nombre: String, correo: String, clave: String, direccion: String) {
        prefs.guardarUsuario(nombre, correo, clave, direccion)
    }

    // --- Obtiene los datos guardados como Flow ---
    fun obtenerDatos(): Flow<Map<String, String>> = prefs.obtenerUsuario

    // --- Limpia los datos del DataStore ---
    suspend fun limpiar() {
        prefs.limpiarDatos()
    }
}
