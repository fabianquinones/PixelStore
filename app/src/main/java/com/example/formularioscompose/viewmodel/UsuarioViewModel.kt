package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.model.UsuarioUIState
import com.example.formularioscompose.model.UsuarioErrores
import com.example.formularioscompose.repository.PerfilRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repo: PerfilRepositorio) : ViewModel() {

    // Estado del formulario
    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado: StateFlow<UsuarioUIState> = _estado

    // ======== MANEJO DE INPUTS ========

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onConfirmarClaveChange(valor: String) {
        _estado.update { it.copy(confirmarClave = valor, errores = it.errores.copy(confirmarClave = null)) }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    // ======== VALIDACIÓN ========

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 8) "Debe tener al menos 8 caracteres" else null,
            confirmarClave = if (estadoActual.clave != estadoActual.confirmarClave)
                "Las claves no coinciden"
            else null,
            direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null,
            terminos = if (!estadoActual.aceptaTerminos)
                "Debes aceptar los términos y condiciones"
            else null
        )



        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.confirmarClave,
            errores.direccion,
            errores.terminos
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // ======== PERSISTENCIA CON DATASTORE ========

    /** Guarda los datos del usuario en DataStore */
    fun guardarUsuario() {
        val e = _estado.value
        viewModelScope.launch {
            repo.guardarDatos(e.nombre, e.correo, e.clave, e.direccion)
        }
    }

    /** Carga los datos del usuario desde DataStore */
    fun cargarUsuario() {
        viewModelScope.launch {
            repo.obtenerDatos().collect { datos ->
                _estado.update {
                    it.copy(
                        nombre = datos["nombre"] ?: "",
                        correo = datos["correo"] ?: "",
                        clave = datos["clave"] ?: "",
                        direccion = datos["direccion"] ?: ""
                    )
                }
            }
        }
    }

    /** Limpia los datos guardados (por ejemplo, al cerrar sesión) */
    fun limpiarDatos() {
        viewModelScope.launch {
            repo.limpiar()
        }
    }
}
