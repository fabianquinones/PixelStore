package com.example.formularioscompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.model.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

class EstadoViewModel(application: Application) : AndroidViewModel(application) {
    private val estadoDataStore = EstadoDataStore(application)

    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo

    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init { cargarEstado() }

    private fun cargarEstado() {
        viewModelScope.launch {
            delay(300)
            _activo.value = estadoDataStore.obtenerEstado().firstOrNull() ?: false
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            val nuevoValor = !(_activo.value ?: false)
            estadoDataStore.guardarEstado(nuevoValor)
            _activo.value = nuevoValor
            _mostrarMensaje.value = true
            delay(1200)
            _mostrarMensaje.value = false
        }
    }
}
