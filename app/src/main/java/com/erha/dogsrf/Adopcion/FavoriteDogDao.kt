package com.erha.dogsrf.Adopcion

import androidx.room.*

@Dao
interface FavoriteDogDao {

    @Insert
    suspend fun insertFavorite(favoriteDog: FavoriteDog)

    @Delete
    suspend fun deleteFavorite(favoriteDog: FavoriteDog)

    @Query("SELECT * FROM favorite_dogs")
    suspend fun getAllFavorites(): List<FavoriteDog>
}
