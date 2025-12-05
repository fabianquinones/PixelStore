package com.example.formularioscompose.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.formularioscompose.model.Product
import com.example.formularioscompose.viewmodel.ProductViewModel
import coil.compose.AsyncImage
import com.example.formularioscompose.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text

// ... (ProductItem Composable se mantiene igual)

@Composable
fun ProductItem(product: Product, onAddToCart: () -> Unit) {
    val chileLocale = Locale("es", "CL")
    val currencyFormat = NumberFormat.getCurrencyInstance(chileLocale)
    currencyFormat.maximumFractionDigits = 0
    val formattedPrice = currencyFormat.format(product.price)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = "Imagen de ${product.name}",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formattedPrice,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onAddToCart) {
                Text("Añadir")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()

) {
    // ⚠️ CAMBIO CLAVE: Recolectar el uiState completo que incluye isLoading y error.
    val uiState by productViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PixelStore") },
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                    IconButton(onClick = { navController.navigate("perfil") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Ver Perfil")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        // ⚠️ CAMBIO CLAVE: Contenedor para manejar los 3 estados (Cargando, Error, Datos)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                // 1. ESTADO DE CARGA: Si isLoading es true
                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                // 2. ESTADO DE ERROR: Si hay un mensaje de error
                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Botón para reintentar la carga
                        Button(onClick = productViewModel::loadProducts) {
                            Text("Reintentar")
                        }
                    }
                }

                // 3. ESTADO DE DATOS: Si la lista de productos tiene elementos
                uiState.products.isNotEmpty() -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp) // Añadir padding si es necesario
                    ) {
                        items(uiState.products) { product ->
                            ProductItem(product = product) {
                                carritoViewModel.addProductToCart(product)
                                println("Añadido al carrito: ${product.name}")
                            }
                        }
                    }
                }

                // 4. LISTA VACÍA (no hay error ni está cargando)
                else -> {
                    Text(text = "No se encontraron productos.")
                }
            }
        }
    }
}