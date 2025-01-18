package com.erha.dogsrf.Adopcion

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_dogs")
data class FavoriteDog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val ubicacion: String,
    val edad: Int,
    val imagenUrl: String
)

