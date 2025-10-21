package com.example.formularioscompose.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Suppress("DEPRECATION")
@Composable
fun InicioSesion(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pixel&Store",
            fontSize = 34.sp,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = emailError != null,
            supportingText = {
                emailError?.let { Text(it) }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                passwordError?.let { Text(it) }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = null
                passwordError = null

                var hasErrors = false

                // Validaciones del correo
                if (email.isBlank()) {
                    emailError = "El correo no puede estar vacío"
                    hasErrors = true
                } else if (!email.contains("@")) {
                    emailError = "El formato del correo no es válido"
                    hasErrors = true
                }

                // Validaciones de la contraseña
                if (password.isBlank()) {
                    passwordError = "La contraseña no puede estar vacía"
                    hasErrors = true
                } else if (password.length < 8) {
                    passwordError = "La contraseña debe tener al menos 8 caracteres"
                    hasErrors = true
                }

                // Ir a la pantalla principal, osea el Home
                if (!hasErrors) {
                    navController.navigate("ProductScreen") {
                        popUpTo("InicioSesion") { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Iniciar Sesion", fontSize = 18.sp)
        }

        //Seccion de Registro, "Registrate aqui"
        Spacer(modifier = Modifier.height(16.dp))

        ClickableText(
            text = buildAnnotatedString {
                append("¿No tienes una cuenta? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("Regístrate aquí")
                }
            },
            onClick = {
                navController.navigate("FormularioScreen")
                println("Clicked on 'Regístrate aquí'")
            }
        )
    }
}

