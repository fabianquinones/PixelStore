package com.example.formularioscompose.repository

import com.example.formularioscompose.remote.ApiClient
import com.example.formularioscompose.remote.dto.CrearUsuarioRequest
import com.example.formularioscompose.remote.dto.UsuarioDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepository {

    private val api = ApiClient.usuarioApi

    // Obtener todos los usuarios (por si lo necesitas)
    suspend fun getUsuarios(): List<UsuarioDto> = withContext(Dispatchers.IO) {
        api.getUsuarios()
    }

    // 🔹 REGISTRO: crea usuario en la API
    suspend fun registrarUsuario(
        nombre: String,
        apellido: String,
        email: String,
        password: String,
        telefono: String,
        direccion: String
    ): UsuarioDto = withContext(Dispatchers.IO) {
        val request = CrearUsuarioRequest(
            nombre = nombre,
            apellido = apellido,
            email = email,
            password = password,
            telefono = telefono,
            direccion = direccion
        )
        api.crearUsuario(request)
    }

    // 🔹 LOGIN básico: busca en la lista por email + password
    suspend fun login(email: String, password: String): UsuarioDto? =
        withContext(Dispatchers.IO) {
            val usuarios = api.getUsuarios()
            usuarios.find { it.email == email && it.password == password }
        }
}
