package com.example.formularioscompose.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "usuario_prefs")

class UsuarioPreferences(private val context: Context) {

    companion object {
        private val NOMBRE = stringPreferencesKey("nombre")
        private val CORREO = stringPreferencesKey("correo")
        private val CLAVE = stringPreferencesKey("clave")
        private val DIRECCION = stringPreferencesKey("direccion")
    }

    suspend fun guardarUsuario(nombre: String, correo: String, clave: String, direccion: String) {
        context.dataStore.edit { prefs ->
            prefs[NOMBRE] = nombre
            prefs[CORREO] = correo
            prefs[CLAVE] = clave
            prefs[DIRECCION] = direccion
        }
    }

    val obtenerUsuario = context.dataStore.data.map { prefs ->
        mapOf(
            "nombre" to (prefs[NOMBRE] ?: ""),
            "correo" to (prefs[CORREO] ?: ""),
            "clave" to (prefs[CLAVE] ?: ""),
            "direccion" to (prefs[DIRECCION] ?: "")
        )
    }

    suspend fun limpiarDatos() {
        context.dataStore.edit { it.clear() }
    }
}
