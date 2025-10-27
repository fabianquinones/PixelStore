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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.formularioscompose.repository.PerfilRepositorio
import com.example.formularioscompose.viewmodel.InicioSesionViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember

@Composable
fun InicioSesion(navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { InicioSesionViewModel(PerfilRepositorio(context)) }
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                value = estado.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = estado.emailError != null,
                supportingText = { estado.emailError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = estado.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.passwordError != null,
                supportingText = { estado.passwordError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (viewModel.validarCampos()) {
                        scope.launch {
                            viewModel.iniciarSesion { exito ->
                                if (exito) {
                                    navController.navigate("ProductScreen") {
                                        popUpTo("InicioSesion") { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "Iniciar Sesión", fontSize = 18.sp)
            }

            estado.loginError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append("¿No tienes una cuenta? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Regístrate aquí")
                    }
                },
                onClick = {
                    if (it >= 23) {
                        navController.navigate("FormularioScreen")
                    }
                }
            )
        }
    }
}
