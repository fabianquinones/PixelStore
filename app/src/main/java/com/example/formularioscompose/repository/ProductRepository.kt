package com.example.formularioscompose.repository

import com.example.formularioscompose.model.Product
import com.example.formularioscompose.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {

    private val localProducts = listOf(
        Product(
            id = 1,
            name = "Razer FireFly v2",
            price = 29990.0,
            imageUrl = "https://i.postimg.cc/6Qzdy4Qr/razer-firefly-v2-hero-mobile.webp"
        ),
        Product(
            id = 2,
            name = "Razer Cobra Pro",
            price = 119990.0,
            imageUrl = "https://i.postimg.cc/htWw0611/Razer-Cobra-Pro.webp"
        ),
        Product(
            id = 3,
            name = "Teclado RedDragon Kumara K552",
            price = 39990.0,
            imageUrl = "https://i.postimg.cc/bJ7MF5jG/kumara-rgb-1.jpg"
        ),
        Product(
            id = 4,
            name = "Monitor Samsung Odyssey G3",
            price = 89990.0,
            imageUrl = "https://i.postimg.cc/4NTq6j5v/Monitor-odyssey.avif"
        )
    )

    private val api = ApiClient.productoApi

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val remote = api.getProductos().map { it.toDomain() }
            // Combina remoto + local (puedes cambiar el orden si quieres)
            remote + localProducts
        } catch (e: Exception) {
            // Si la API falla → al menos se ven los locales
            localProducts
        }
    }
}
