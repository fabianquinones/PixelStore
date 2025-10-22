package com.example.formularioscompose.view.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Esquema para el modo claro (no lo usaremos por ahora, pero es bueno tenerlo).
private val LightColorScheme = lightColorScheme(
    primary = VerdeChillon,
    secondary = GrisSuperficie,
    tertiary = VerdeChillon
)

// TU ESQUEMA DE COLORES OSCURO: Usa los colores definidos en Color.kt
private val DarkColorScheme = darkColorScheme(
    primary = VerdeChillon,           // Botones, bordes de TextField activos, enlaces.
    background = GrisOscuroFondo,     // Fondo principal de la app.
    surface = GrisSuperficie,         // Color de fondo para Cards, TextFields, etc.
    onPrimary = NegroAbsoluto,        // Color del texto DENTRO de los botones.
    onBackground = TextoBlanco,       // Color del texto normal sobre el fondo oscuro.
    onSurface = TextoBlanco,          // Color del texto y los iconos sobre las "superficies".
    outline = VerdeChillon            // Color para los bordes, como el de OutlinedTextField.
)

@Composable
fun FormulariosComposeTheme(
    // CAMBIO CLAVE 1: Asegúrate de que 'darkTheme' esté forzado a 'true'.
    darkTheme: Boolean = true,// CAMBIO CLAVE 2: Asegúrate de que 'dynamicColor' esté en 'false'.
    // Esto evita que el color del fondo de pantalla de Android interfiera.
    dynamicColor: Boolean = false,

    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Al estar darkTheme=true, siempre entrará aquí:
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // ... resto del archivo sin cambios ...

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

