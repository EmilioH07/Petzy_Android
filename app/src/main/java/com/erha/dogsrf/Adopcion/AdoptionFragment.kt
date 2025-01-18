package com.erha.dogsrf.Adopcion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.erha.dogsrf.R
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdoptionFragment : Fragment(R.layout.fragment_adoption) {

    private lateinit var adapter: DogsAdoptionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: DogService
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = requireActivity().findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayout)

        // Crear un nuevo LayoutParams con márgenes ajustados
        val params = tabLayout.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 8 // Reducir margen superior
        tabLayout.layoutParams = params

        // Inicializamos SharedPreferencesManager
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // Inicializamos el RecyclerView y el adaptador
        recyclerView = view.findViewById(R.id.rvDogs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DogsAdoptionAdapter(emptyList(), sharedPreferencesManager)
        recyclerView.adapter = adapter

        // Inicializamos Retrofit (API)
        api = RetrofitInstance.api

        // Cargar los datos de la API
        fetchDogsData()

        // Configurar el botón de "Favoritos"
        // Configurar el botón de "Favoritos"
        val btnFavorites = view.findViewById<Button>(R.id.btnFavorites)
        btnFavorites.setOnClickListener {
            // Pasamos el sharedPreferencesManager al crear el dialogo
            val favoriteDialogFragment = FavoriteDialogFragment.newInstance(sharedPreferencesManager)

            // Mostrar el fragmento en forma de diálogo
            favoriteDialogFragment.show(requireActivity().supportFragmentManager, "FavoriteDialog")
        }

    }

    // Función para obtener los datos desde la API
    private fun fetchDogsData() {
        api.getDogsList() // Llamamos a la función definida en DogService
            .enqueue(object : Callback<List<DogAdoption>> {
                override fun onResponse(call: Call<List<DogAdoption>>, response: Response<List<DogAdoption>>) {
                    if (response.isSuccessful) {
                        // Actualizamos la lista con los datos de la API
                        val dogList = response.body() ?: emptyList()
                        adapter.updateData(dogList)
                    } else {
                        // Manejo de errores (por ejemplo, mostrar un mensaje)
                        Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DogAdoption>>, t: Throwable) {
                    // Manejo de fallos en la conexión o en la API
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}











