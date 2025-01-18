package com.erha.dogsrf.Adopcion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erha.dogsrf.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment(private val sharedPreferencesManager: SharedPreferencesManager) : Fragment(R.layout.fragment_favorite) {

    private lateinit var adapter: DogsAdoptionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: DogService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.rvFavoriteDogs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DogsAdoptionAdapter(emptyList(), sharedPreferencesManager)
        recyclerView.adapter = adapter

        // Inicializar Retrofit (API)
        api = RetrofitInstance.api

        // Cargar los perros favoritos
        fetchFavoriteDogs()

        // Configurar botón para cerrar
        val btnClose = view.findViewById<Button>(R.id.btnCloseFavoriteDialog)
        btnClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    // Función para obtener los perros favoritos desde SharedPreferences
    private fun fetchFavoriteDogs() {
        api.getDogsList() // Llamamos a la API para obtener todos los perros
            .enqueue(object : Callback<List<DogAdoption>> {
                override fun onResponse(call: Call<List<DogAdoption>>, response: Response<List<DogAdoption>>) {
                    if (response.isSuccessful) {
                        val dogList = response.body() ?: emptyList()

                        // Filtrar los perros que están marcados como favoritos
                        val favoriteDogs = dogList.filter { dog ->
                            sharedPreferencesManager.isFavorite(dog.id)
                        }

                        // Actualizar el adaptador con los perros favoritos
                        adapter.updateData(favoriteDogs)
                    } else {
                        // Manejo de errores
                        Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DogAdoption>>, t: Throwable) {
                    // Manejo de fallos en la conexión o en la API
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    companion object {
        // Método para crear una nueva instancia del fragmento pasando los parámetros necesarios
        fun newInstance(sharedPreferencesManager: SharedPreferencesManager): FavoriteFragment {
            return FavoriteFragment(sharedPreferencesManager)
        }
    }
}

