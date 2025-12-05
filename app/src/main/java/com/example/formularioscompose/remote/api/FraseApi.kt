package com.example.formularioscompose.remote.api

import com.example.formularioscompose.remote.dto.FraseDto
import retrofit2.http.GET

interface FraseApi {

    @GET("random")
    suspend fun getFraseAleatoria(): FraseDto
}
