package com.example.formularioscompose.remote.dto

data class UsuarioDto(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val telefono: String,
    val direccion: String,
    val activo: Boolean
)
