package com.example.formularioscompose.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.formularioscompose.viewmodel.UsuarioViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import kotlinx.coroutines.launch

@Composable
fun FormularioScreen(navController: NavController, viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()     // 👈 Necesario para registrarUsuario()

    // Estados locales para visibilidad de las contraseñas
    var claveVisible by remember { mutableStateOf(false) }
    var confirmarVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Formulario de Registro",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // NOMBRE
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = estado.errores.nombre != null,
                supportingText = { estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth()
            )

            // APELLIDO
            OutlinedTextField(
                value = estado.apellido,
                onValueChange = viewModel::onApellidoChange,
                label = { Text("Apellido") },
                isError = estado.errores.apellido != null,
                supportingText = { estado.errores.apellido?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth()
            )

            // CORREO
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo") },
                isError = estado.errores.correo != null,
                supportingText = { estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth()
            )

            // TELÉFONO
            OutlinedTextField(
                value = estado.telefono,
                onValueChange = viewModel::onTelefonoChange,
                label = { Text("Teléfono") },
                isError = estado.errores.telefono != null,
                supportingText = {
                    estado.errores.telefono?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // CLAVE
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Clave") },
                isError = estado.errores.clave != null,
                supportingText = { estado.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (claveVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { claveVisible = !claveVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (claveVisible) "Ocultar clave" else "Mostrar clave"
                        )
                    }
                }
            )

            // CONFIRMAR CLAVE
            OutlinedTextField(
                value = estado.confirmarClave,
                onValueChange = viewModel::onConfirmarClaveChange,
                label = { Text("Confirmar clave") },
                isError = estado.errores.confirmarClave != null,
                supportingText = { estado.errores.confirmarClave?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmarVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (confirmarVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { confirmarVisible = !confirmarVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (confirmarVisible) "Ocultar clave" else "Mostrar clave"
                        )
                    }
                }
            )

            // DIRECCIÓN
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección") },
                isError = estado.errores.direccion != null,
                supportingText = { estado.errores.direccion?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth()
            )

            // CHECKBOX
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text("Acepto los términos y condiciones")
            }

            estado.errores.terminos?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // BOTÓN PRINCIPAL — REGISTRA EN LA API
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        scope.launch {
                            val exito = viewModel.registrarUsuario()   // 👈 ahora sí usa la API
                            if (exito) {
                                navController.navigate("RegistroExitosoScreen")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Validar y continuar")
            }
        }
    }
}


