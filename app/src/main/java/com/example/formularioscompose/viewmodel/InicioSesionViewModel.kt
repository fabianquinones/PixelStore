package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null
)

class InicioSesionViewModel(private val repo: PerfilRepositorio) : ViewModel() {

    private val usuarioRepo = UsuarioRepository()

    private val _estado = MutableStateFlow(LoginUIState())
    val estado: StateFlow<LoginUIState> = _estado

    fun onEmailChange(valor: String) {
        _estado.update { it.copy(email = valor, emailError = null, loginError = null) }
    }

    fun onPasswordChange(valor: String) {
        _estado.update { it.copy(password = valor, passwordError = null, loginError = null) }
    }

    fun validarCampos(): Boolean {
        val e = _estado.value
        var emailError: String? = null
        var passwordError: String? = null

        if (e.email.isBlank()) emailError = "El correo no puede estar vacío"
        else if (!e.email.contains("@")) emailError = "Formato de correo inválido"

        if (e.password.isBlank()) passwordError = "La contraseña no puede estar vacía"
        else if (e.password.length < 8) passwordError = "Debe tener al menos 8 caracteres"

        _estado.update { it.copy(emailError = emailError, passwordError = passwordError) }

        return emailError == null && passwordError == null
    }

    /**
     * LOGIN: depende solo de la API.
     */
    fun iniciarSesion(onResultado: (Boolean) -> Unit) {
        val e = _estado.value

        viewModelScope.launch {
            if (!validarCampos()) {
                onResultado(false)
                return@launch
            }

            try {
                val usuario = usuarioRepo.login(e.email, e.password)

                if (usuario != null) {
                    // Guardar usuario logueado en DataStore (para PerfilScreen)
                    repo.guardarDatos(
                        nombre = usuario.nombre,
                        apellido = usuario.apellido,
                        correo = usuario.email,
                        telefono = usuario.telefono,
                        clave = e.password,           // la API no devuelve password
                        direccion = usuario.direccion
                    )

                    onResultado(true)
                } else {
                    _estado.update { it.copy(loginError = "Correo o contraseña incorrectos") }
                    onResultado(false)
                }
            } catch (_: Exception) {
                _estado.update { it.copy(loginError = "Error al conectar con el servidor") }
                onResultado(false)
            }
        }
    }
}
