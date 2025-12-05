package com.example.formularioscompose.viewmodel

import com.example.formularioscompose.CoroutineTestRule
import com.example.formularioscompose.remote.dto.FraseDto
import com.example.formularioscompose.repository.FraseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class FraseViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val repo: FraseRepository = mock()

    @Test
    fun `cargarFrase éxito actualiza frase y limpia error`() = runTest {
        // 👇 lo que la "API externa" respondería
        whenever(repo.obtenerFraseAleatoria())
            .thenReturn(FraseDto(content = "hola mundo", author = "autor X"))

        // ViewModel usando el repo mockeado
        val vm = FraseViewModel(repo)

        // avanzar corrutinas (init ya llamó cargarFrase())
        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.frase.value)
        assertEquals("hola mundo", vm.frase.value?.content)
        assertEquals("autor X", vm.frase.value?.author)
        assertNull(vm.error.value)
    }

    @Test
    fun `cargarFrase error limpia frase y setea mensaje de error`() = runTest {
        whenever(repo.obtenerFraseAleatoria())
            .thenThrow(RuntimeException("falló la api"))

        val vm = FraseViewModel(repo)

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertNull(vm.frase.value)
        assertNotNull(vm.error.value)
        assertTrue(vm.error.value!!.contains("Error"))
    }
}
