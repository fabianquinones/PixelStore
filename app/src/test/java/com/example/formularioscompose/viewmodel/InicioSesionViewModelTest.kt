package com.example.formularioscompose.viewmodel

import com.example.formularioscompose.CoroutineTestRule
import com.example.formularioscompose.remote.dto.UsuarioDto
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.repository.UsuarioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class InicioSesionViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val perfilRepo: PerfilRepositorio = mock()
    private val usuarioRepo: UsuarioRepository = mock()

    /**
     * Truco para inyectar el repo mock al ViewModel:
     * reemplazamos el usuarioRepo por reflexión
     */
    private fun crearViewModel(): InicioSesionViewModel {
        val vm = InicioSesionViewModel(perfilRepo)

        val field = InicioSesionViewModel::class.java.getDeclaredField("usuarioRepo")
        field.isAccessible = true
        field.set(vm, usuarioRepo)

        return vm
    }

    @Test
    fun `validarCampos falla si email y password estan vacios`() {
        val vm = crearViewModel()

        val resultado = vm.validarCampos()

        assertFalse(resultado)
        assertNotNull(vm.estado.value.emailError)
        assertNotNull(vm.estado.value.passwordError)
    }

    @Test
    fun `login correcto devuelve true`() = runTest {
        val vm = crearViewModel()

        val usuarioFake = UsuarioDto(
            id = 1,
            nombre = "Test",
            apellido = "User",
            email = "test@test.cl",
            password = "holamund0",
            telefono = "123456789",
            direccion = "Calle falsa",
            activo = true
        )

        whenever(usuarioRepo.login("test@test.cl", "holamund0"))
            .thenReturn(usuarioFake)

        vm.onEmailChange("test@test.cl")
        vm.onPasswordChange("holamund0")

        var resultado: Boolean? = null

        vm.iniciarSesion {
            resultado = it
        }

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertTrue(resultado == true)

        verify(usuarioRepo).login("test@test.cl", "holamund0")
        verify(perfilRepo).guardarDatos(
            usuarioFake.nombre,
            usuarioFake.apellido,
            usuarioFake.email,
            usuarioFake.telefono,
            "holamund0",
            usuarioFake.direccion
        )
    }

    @Test
    fun `login invalido devuelve false`() = runTest {
        val vm = crearViewModel()

        whenever(usuarioRepo.login(any(), any()))
            .thenReturn(null)

        vm.onEmailChange("fake@test.cl")
        vm.onPasswordChange("incorrecto")

        var resultado: Boolean? = null

        vm.iniciarSesion {
            resultado = it
        }

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertFalse(resultado ?: true)
    }
}
