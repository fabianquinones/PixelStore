package com.example.formularioscompose.repository

import com.example.formularioscompose.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


object CarritoRepository {

    private val _cartItems = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Product, Int>> = _cartItems.asStateFlow()

    fun addProductToCart(product: Product) {
        _cartItems.update { currentItems ->
            val newItems = currentItems.toMutableMap()
            val currentQuantity = newItems[product] ?: 0
            newItems[product] = currentQuantity + 1
            newItems.toMap()
        }
    }

    fun removeOneUnitFromCart(product: Product) {
        _cartItems.update { currentItems ->
            val newItems = currentItems.toMutableMap()

            val currentQuantity = newItems[product] ?: 0

            if (currentQuantity > 1) {
                newItems[product] = currentQuantity - 1
            } else {
                newItems.remove(product)
            }

            newItems.toMap()
        }
    }

    fun removeProductFromCart(product: Product) {
        _cartItems.update { currentItems ->
            currentItems.toMutableMap().apply {
                remove(product)
            }.toMap()
        }
    }
}