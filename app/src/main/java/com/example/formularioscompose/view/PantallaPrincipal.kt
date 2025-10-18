// Demo de DataStore + animación (no enlazada en el NavHost por defecto)
package com.example.formularioscompose.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.formularioscompose.viewmodel.EstadoViewModel

@Composable
fun PantallaPrincipal(vm: EstadoViewModel = viewModel()) {
    val estado = vm.activo.collectAsState()
    val mensaje = vm.mostrarMensaje.collectAsState()

    if (estado.value == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val activo = estado.value!!
        val colorAnimado by animateColorAsState(
            targetValue = if (activo) Color(0xFF4CAF50) else Color(0xFFB0BEC5),
            animationSpec = tween(500), label = "colorModo"
        )
        val textoBoton = if (activo) "Desactivar" else "Activar"

        Column(
            Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { vm.alternarEstado() },
                colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text(textoBoton, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(24.dp))
            AnimatedVisibility(visible = mensaje.value) {
                Text("¡Estado guardado exitosamente!", color = Color(0xFF4CAF50))
            }
        }
    }
}
