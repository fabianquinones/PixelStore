package com.example.formularioscompose.repository

import com.example.formularioscompose.remote.ApiClient
import com.example.formularioscompose.remote.dto.FraseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FraseRepository {

    private val api = ApiClient.fraseApi

    suspend fun obtenerFraseAleatoria(): FraseDto = withContext(Dispatchers.IO) {
        api.getFraseAleatoria()
    }
}
