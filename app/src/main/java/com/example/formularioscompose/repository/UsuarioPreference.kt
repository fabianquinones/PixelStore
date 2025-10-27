package com.example.formularioscompose.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "usuario_prefs")

data class Usuario(
    val nombre: String,
    val correo: String,
    val clave: String,
    val direccion: String
)

class UsuarioPreferences(private val context: Context) {

    companion object {
        private val USUARIOS = stringPreferencesKey("usuarios_json")
    }

    private val gson = Gson()

    // 🔹 Guarda un nuevo usuario (se agrega a la lista existente)
    suspend fun guardarUsuario(nuevoUsuario: Usuario) {
        context.dataStore.edit { prefs ->
            val listaActualJson = prefs[USUARIOS]
            val listaActual: MutableList<Usuario> = if (listaActualJson != null) {
                val type = object : TypeToken<MutableList<Usuario>>() {}.type
                gson.fromJson(listaActualJson, type)
            } else {
                mutableListOf()
            }

            // Agregar el nuevo usuario
            listaActual.add(nuevoUsuario)

            // Guardar de nuevo como JSON
            prefs[USUARIOS] = gson.toJson(listaActual)
        }
    }

    // 🔹 Obtiene la lista completa de usuarios guardados
    val obtenerUsuarios = context.dataStore.data.map { prefs ->
        val listaJson = prefs[USUARIOS]
        if (listaJson != null) {
            val type = object : TypeToken<List<Usuario>>() {}.type
            gson.fromJson<List<Usuario>>(listaJson, type)
        } else {
            emptyList()
        }
    }

    // 🔹 Limpia todos los usuarios
    suspend fun limpiarDatos() {
        context.dataStore.edit { it.clear() }
    }
}
