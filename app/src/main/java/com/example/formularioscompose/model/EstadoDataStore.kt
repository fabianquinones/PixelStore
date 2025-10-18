package com.example.formularioscompose.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EstadoDataStore(private val context: Context) {
    companion object {
        val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")
    }

    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ESTADO_ACTIVADO] = valor
        }
    }

    fun obtenerEstado(): Flow<Boolean?> =
        context.dataStore.data.map { prefs -> prefs[ESTADO_ACTIVADO] }
}
