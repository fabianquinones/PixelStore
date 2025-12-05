package com.example.formularioscompose.remote.dto

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    val imagen: String,
    val destacado: Boolean,
    val categoriaId: Int
) {
    fun toDomain() = com.example.formularioscompose.model.Product(
        id = id,
        name = nombre,
        price = precio.toDouble(),
        imageUrl = imagen
    )
}
