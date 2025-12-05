package com.example.formularioscompose.remote.api

import com.example.formularioscompose.remote.dto.ProductoDto
import retrofit2.http.GET

interface ProductoApi {
    @GET("productos")
    suspend fun getProductos(): List<ProductoDto>
}
