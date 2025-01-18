package com.erha.dogsrf.Adopcion

import android.content.Context
import android.content.SharedPreferences
import java.io.Serializable

class SharedPreferencesManager(context: Context) : Serializable {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    // Verifica si un perro es favorito
    fun isFavorite(dogId: String): Boolean {
        return sharedPreferences.getBoolean(dogId, false)
    }

    // Guarda el estado de favorito
    fun saveFavorite(dogId: String, isFavorite: Boolean) {
        sharedPreferences.edit().putBoolean(dogId, isFavorite).apply()
    }
}

