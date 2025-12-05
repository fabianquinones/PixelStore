package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.remote.dto.FraseDto
import com.example.formularioscompose.repository.FraseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FraseViewModel(
    private val repo: FraseRepository = FraseRepository()
) : ViewModel() {

    private val _frase = MutableStateFlow<FraseDto?>(null)
    val frase: StateFlow<FraseDto?> = _frase

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarFrase()
    }

    fun cargarFrase() {
        viewModelScope.launch {
            try {
                val resultado = repo.obtenerFraseAleatoria()
                _frase.value = resultado
                _error.value = null
            } catch (e: Exception) {
                _frase.value = null
                _error.value = "Error: ${e::class.simpleName} - ${e.message}"
                e.printStackTrace()
            }
        }
    }
}
