package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formularioscompose.model.Product
import com.example.formularioscompose.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// El estado de la pantalla
data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts() // Carga los productos al iniciar el ViewModel
    }

    fun loadProducts() {
        viewModelScope.launch {
            // 1. Iniciar carga: Cambiar estado a 'cargando'
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // 2. Llamar al repositorio para obtener datos de la red
                val productos = repository.getProducts()

                // 3. Éxito: Actualizar estado con los productos
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    products = productos
                )
            } catch (e: Exception) {
                // 4. Error: Actualizar estado con mensaje de error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar productos: ${e.message}"
                )
            }
        }
    }
}