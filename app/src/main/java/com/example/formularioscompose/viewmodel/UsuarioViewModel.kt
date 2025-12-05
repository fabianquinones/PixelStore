package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.model.UsuarioErrores
import com.example.formularioscompose.model.UsuarioUIState
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repo: PerfilRepositorio) : ViewModel() {

    private val usuarioRepo = UsuarioRepository()

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado: StateFlow<UsuarioUIState> = _estado

    // bandera para evitar doble registro
    private val _registrando = MutableStateFlow(false)
    val registrando: StateFlow<Boolean> = _registrando

    // ================== MANEJO DE INPUTS ==================

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onApellidoChange(valor: String) {
        _estado.update {
            it.copy(
                apellido = valor,
                errores = it.errores.copy(apellido = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onTelefonoChange(valor: String) {
        _estado.update {
            it.copy(
                telefono = valor,
                errores = it.errores.copy(telefono = null)
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    fun onConfirmarClaveChange(valor: String) {
        _estado.update {
            it.copy(
                confirmarClave = valor,
                errores = it.errores.copy(confirmarClave = null)
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(
                direccion = valor,
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(
                aceptaTerminos = valor,
                errores = it.errores.copy(terminos = null)
            )
        }
    }

    // ================== VALIDACIÓN ==================

    fun validarFormulario(): Boolean {
        val e = _estado.value

        val errores = UsuarioErrores(
            nombre = if (e.nombre.isBlank()) "Campo obligatorio" else null,
            apellido = if (e.apellido.isBlank()) "Campo obligatorio" else null,
            correo = if (!e.correo.contains("@")) "Correo inválido" else null,
            clave = if (e.clave.length < 8) "Debe tener al menos 8 caracteres" else null,
            confirmarClave = if (e.clave != e.confirmarClave)
                "Las claves no coinciden"
            else null,
            direccion = if (e.direccion.isBlank()) "Campo obligatorio" else null,
            telefono = if (e.telefono.isBlank()) "Campo obligatorio" else null,
            terminos = if (!e.aceptaTerminos)
                "Debes aceptar los términos y condiciones"
            else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.apellido,
            errores.correo,
            errores.clave,
            errores.confirmarClave,
            errores.direccion,
            errores.telefono,
            errores.terminos
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    // ================== REGISTRO (API + guardar usuario actual) ==================

    suspend fun registrarUsuario(): Boolean {
        if (_registrando.value) return false
        _registrando.value = true

        val e = _estado.value

        return try {
            val existentes = usuarioRepo.getUsuarios()
            val yaExiste = existentes.any { it.email.equals(e.correo, ignoreCase = true) }

            if (yaExiste) {
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(
                            correo = "Este correo ya está registrado"
                        )
                    )
                }
                false
            } else {
                // 1) Crear en la API
                val creado = usuarioRepo.registrarUsuario(
                    nombre = e.nombre,
                    apellido = e.apellido,
                    email = e.correo,
                    password = e.clave,
                    telefono = e.telefono,
                    direccion = e.direccion
                )

                // 2) Actualizar estado local
                _estado.update {
                    it.copy(
                        nombre = creado.nombre,
                        apellido = creado.apellido,
                        correo = creado.email,
                        telefono = creado.telefono,
                        direccion = creado.direccion
                    )
                }

                // 3) Guardar usuario actual en DataStore (para PerfilScreen)
                repo.guardarDatos(
                    nombre = creado.nombre,
                    apellido = creado.apellido,
                    correo = creado.email,
                    telefono = creado.telefono,
                    clave = e.clave,
                    direccion = creado.direccion
                )

                true
            }
        } catch (_: Exception) {
            false
        } finally {
            _registrando.value = false
        }
    }

    // ================== LEER USUARIO PARA PERFIL ==================

    fun cargarUsuario() {
        viewModelScope.launch {
            repo.obtenerDatos().collect { lista ->
                if (lista.isNotEmpty()) {
                    val ultimo = lista.last()
                    _estado.update {
                        it.copy(
                            nombre = ultimo.nombre,
                            apellido = ultimo.apellido,
                            correo = ultimo.correo,
                            telefono = ultimo.telefono,
                            clave = ultimo.clave,
                            direccion = ultimo.direccion
                        )
                    }
                }
            }
        }
    }

    fun limpiarDatos() {
        viewModelScope.launch {
            repo.limpiar()
        }
    }
}
