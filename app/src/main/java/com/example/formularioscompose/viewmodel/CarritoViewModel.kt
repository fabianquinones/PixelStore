package com.example.formularioscompose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.formularioscompose.model.Product
import com.example.formularioscompose.repository.CarritoRepository
import kotlinx.coroutines.flow.StateFlow

class CarritoViewModel : ViewModel() {
    private val repository = CarritoRepository
    val cartItems: StateFlow<Map<Product, Int>> = repository.cartItems

    fun addProductToCart(product: Product) {
        repository.addProductToCart(product)
    }

    fun removeProductFromCart(product: Product) {
        repository.removeProductFromCart(product)
    }

    fun removeOneUnitFromCart(product: Product) {
        repository.removeOneUnitFromCart(product)
    }
}