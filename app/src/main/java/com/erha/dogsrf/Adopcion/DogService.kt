package com.erha.dogsrf.Adopcion

import retrofit2.Call
import retrofit2.http.GET

interface DogService {
    @GET("dogs/dog_list")
    fun getDogsList(): Call<List<DogAdoption>>
}
