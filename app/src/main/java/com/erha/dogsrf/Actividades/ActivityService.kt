package com.erha.dogsrf.Actividades

import retrofit2.http.GET
import retrofit2.http.Path

interface ActivityService {

    // Método para obtener los lugares de la primera API (run places)
    @GET("activities/run_list")
    suspend fun getWalkPlaces(): List<Activity>  // Este accede a la API de actividades de correr

    // Método para obtener los detalles de una actividad específica (por ID) de la primera API
    @GET("activities/run_list/{id}")  // Ruta que usa el parámetro 'id' de la primera API
    suspend fun getActivityDetails(@Path("id") id: String): ActivityDetails

    // Método para obtener los lugares de la segunda API (plaza places)
    @GET("activities/plazas_list")  // Accede a la API de plazas
    suspend fun getPlazaPlaces(): List<Activity>  // Este accede a la API de plazas

    // Método para obtener los detalles de una actividad específica (por ID) de la segunda API
    @GET("activities/plazas_list/{id}")  // Ruta que usa el parámetro 'id' de la segunda API
    suspend fun getPlazaActivityDetails(@Path("id") id: String): ActivityDetails  // Nuevo método
}



