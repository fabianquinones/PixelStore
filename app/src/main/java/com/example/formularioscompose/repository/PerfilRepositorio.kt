package com.example.formularioscompose.repository

import android.content.Context

class PerfilRepositorio(context: Context) {

    private val prefs = UsuarioPreferences(context)

    suspend fun guardarDatos(
        nombre: String,
        apellido: String,
        correo: String,
        telefono: String,
        clave: String,
        direccion: String
    ) {
        val usuario = Usuario(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            telefono = telefono,
            clave = clave,
            direccion = direccion
        )
        prefs.guardarUsuario(usuario)
    }

    fun obtenerDatos() = prefs.obtenerUsuarios

    suspend fun limpiar() {
        prefs.limpiarDatos()
    }
}

