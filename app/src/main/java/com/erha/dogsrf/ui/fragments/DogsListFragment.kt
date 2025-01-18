package com.erha.dogsrf.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.erha.dogsrf.R
import com.erha.dogsrf.application.DogsRFApp
import com.erha.dogsrf.data.DogRepository
import com.erha.dogsrf.data.remote.model.DogDetailDto
import com.erha.dogsrf.data.remote.model.DogDto
import com.erha.dogsrf.databinding.FragmentDogsListBinding
import com.erha.dogsrf.ui.adapters.DogsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogsListFragment : Fragment() {

    private var _binding: FragmentDogsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DogRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener la instancia del repositorio
        repository = (requireActivity().application as DogsRFApp).repository

        // Mostrar la imagen de carga
        binding.ivLoading.visibility = View.VISIBLE
        binding.rvDogs.visibility = View.GONE

        // Obtener la lista de perros desde la API
        fetchDogList()
    }

    private fun fetchDogList() {
        val call: Call<List<DogDto>> = repository.getDogList()

        call.enqueue(object : Callback<List<DogDto>> {
            override fun onResponse(call: Call<List<DogDto>>, response: Response<List<DogDto>>) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        response.body()?.let { dogs ->
                            setupRecyclerView(dogs)
                        } ?: run {
                            showToast("No se encontraron perros")
                        }
                    } else {
                        showToast("Error: ${response.message()}")
                    }
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<List<DogDto>>, t: Throwable) {
                if (isAdded) {
                    showToast("Error: No hay conexión disponible")
                    hideLoading()
                }
            }
        })
    }

    private fun setupRecyclerView(dogs: List<DogDto>) {
        val mutableDogsList = dogs.toMutableList()

        binding.rvDogs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DogsAdapter(mutableDogsList) { dog ->
                dog.id?.let { id ->
                    // Obtener los detalles del perro usando la ID
                    fetchDogDetails(id)
                }
            }
            visibility = View.VISIBLE
        }
    }

    private fun fetchDogDetails(dogId: String) {
        repository.getDogDetail(dogId).enqueue(object : Callback<DogDetailDto> {
            override fun onResponse(call: Call<DogDetailDto>, response: Response<DogDetailDto>) {
                if (response.isSuccessful) {
                    response.body()?.let { dogDetails ->
                        showDogDetailDialog(dogDetails)  // Pasamos el DogDetailDto
                    }
                } else {
                    showToast("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DogDetailDto>, t: Throwable) {
                showToast("Error: No hay conexión disponible")
            }
        })
    }



    private fun showDogDetailDialog(dogDetails: DogDetailDto) {
        val dialogFragment = DogDetailDialogFragment.newInstance(dogDetails)  // Creamos un fragmento con DogDetailDto
        dialogFragment.show(childFragmentManager, "DogDetailDialog")
    }


    private fun hideLoading() {
        binding.ivLoading.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}











