package com.example.formularioscompose.viewmodel

import com.example.formularioscompose.repository.PerfilRepositorio
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock

class UsuarioViewModelTest {

    // Mock porque el ViewModel lo pide, pero en estos tests no usamos DataStore
    private val perfilRepo: PerfilRepositorio = mock()

    private val viewModel = UsuarioViewModel(perfilRepo)

    @Test
    fun `validarFormulario falla cuando todos los campos estan vacios`() {
        // estado inicial: todo vacío

        val resultado = viewModel.validarFormulario()

        // 1) Debe ser falso
        assertFalse(resultado)

        // 2) Debe haber al menos UN error en algún campo
        val errores = viewModel.estado.value.errores

        val listaErrores = listOf(
            errores.nombre,
            errores.apellido,
            errores.correo,
            errores.clave,
            errores.confirmarClave,
            errores.direccion,
            errores.telefono,
            errores.terminos
        )

        assertTrue(
            "Se esperaba al menos un mensaje de error cuando el formulario está vacío",
            listaErrores.any { it != null }
        )
    }

    @Test
    fun `validarFormulario pasa cuando todos los campos son correctos`() {
        // Simulamos que el usuario llenó el formulario
        viewModel.onNombreChange("Kamila")
        viewModel.onApellidoChange("Ingrid")
        viewModel.onCorreoChange("test@test.cl")
        viewModel.onTelefonoChange("123456789")
        viewModel.onClaveChange("holamund0")
        viewModel.onConfirmarClaveChange("holamund0")
        viewModel.onDireccionChange("Calle Falsa 123")
        viewModel.onAceptarTerminosChange(true)

        val resultado = viewModel.validarFormulario()

        // Debe ser true
        assertTrue(resultado)

        val errores = viewModel.estado.value.errores
        assertNull(errores.nombre)
        assertNull(errores.apellido)
        assertNull(errores.correo)
        assertNull(errores.clave)
        assertNull(errores.confirmarClave)
        assertNull(errores.direccion)
        assertNull(errores.telefono)
        assertNull(errores.terminos)
    }
}
