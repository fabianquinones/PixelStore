package com.example.formularioscompose.model

data class UsuarioUIState(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)

