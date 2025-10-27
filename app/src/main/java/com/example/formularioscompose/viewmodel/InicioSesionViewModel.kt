package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.repository.PerfilRepositorio
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

    private val _estado = MutableStateFlow(LoginUIState())
    val estado: StateFlow<LoginUIState> = _estado

    // ===== Manejo de inputs =====

    fun onEmailChange(valor: String) {
        _estado.update { it.copy(email = valor, emailError = null, loginError = null) }
    }

    fun onPasswordChange(valor: String) {
        _estado.update { it.copy(password = valor, passwordError = null, loginError = null) }
    }

    // ===== Validación de campos =====

    fun validarCampos(): Boolean {
        val e = _estado.value
        var emailError: String? = null
        var passwordError: String? = null

        if (e.email.isBlank()) emailError = "El correo no puede estar vacío"
        else if (!e.email.contains("@")) emailError = "El formato del correo no es válido"

        if (e.password.isBlank()) passwordError = "La contraseña no puede estar vacía"
        else if (e.password.length < 8) passwordError = "Debe tener al menos 8 caracteres"

        val hayErrores = emailError != null || passwordError != null

        _estado.update { it.copy(emailError = emailError, passwordError = passwordError) }

        return !hayErrores
    }

    // ===== Verificar credenciales con DataStore =====

    fun iniciarSesion(onResultado: (Boolean) -> Unit) {
        val e = _estado.value
        viewModelScope.launch {
            repo.obtenerDatos().collect { listaUsuarios ->
                val usuarioEncontrado = listaUsuarios.any {
                    it.correo == e.email && it.clave == e.password
                }

                if (usuarioEncontrado) {
                    onResultado(true)
                } else {
                    _estado.update { it.copy(loginError = "Correo o contraseña incorrectos") }
                    onResultado(false)
                }
            }
        }
    }
}
