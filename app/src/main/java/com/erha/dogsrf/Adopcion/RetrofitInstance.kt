package com.erha.dogsrf.Adopcion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://private-2ba523-adoptions.apiary-mock.com/"

    val api: DogService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogService::class.java)
    }
}
