package com.example.formularioscompose.remote.api

import com.example.formularioscompose.remote.dto.UsuarioDto
import com.example.formularioscompose.remote.dto.CrearUsuarioRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioApi {

    @GET("usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @POST("usuarios")
    suspend fun crearUsuario(@Body request: CrearUsuarioRequest): UsuarioDto
}
