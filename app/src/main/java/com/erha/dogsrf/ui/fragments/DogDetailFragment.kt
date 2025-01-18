package com.erha.dogsrf.ui.fragments

import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.erha.dogsrf.R
import com.erha.dogsrf.application.DogsRFApp
import com.erha.dogsrf.data.DogRepository
import com.erha.dogsrf.data.remote.model.DogDetailDto
import com.erha.dogsrf.databinding.FragmentDogDetailBinding
import com.erha.dogsrf.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class DogDetailFragment : Fragment() {

    private var dogId: String? = null
    private var _binding: FragmentDogDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            dogId = args.getString(DOG_ID)
            Log.d(Constants.LOGTAG, "ID recibido: $dogId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos la vista del fragmento y asignamos el binding
        _binding = FragmentDogDetailBinding.inflate(inflater, container, false)
        return binding.root  // Retornamos la vista asociada con el binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verificamos que el dogId esté disponible
        dogId?.let { id ->
            // Inicializamos el repositorio y llamamos a la API
            repository = (requireActivity().application as DogsRFApp).repository

            val call: Call<DogDetailDto> = repository.getDogDetail(id)

            call.enqueue(object : Callback<DogDetailDto> {
                override fun onResponse(call: Call<DogDetailDto>, response: Response<DogDetailDto>) {
                    if (response.isSuccessful && response.body() != null) {
                        val dogDetail = response.body()
                        binding.apply {
                            tvTitle.text = dogDetail?.title
                            tvSize.text = dogDetail?.size
                            tvDaily.text = dogDetail?.dailyfood
                            tvLife.text = dogDetail?.lifeexpectancy
                            tvCoat.text = dogDetail?.coattype
                            tvTemperament.text = dogDetail?.temperament
                            tvExercise.text = dogDetail?.exerciseneeds

                            Glide.with(requireContext())
                                .load(dogDetail?.image)
                                .into(ivImage)
                        }
                    } else {
                        Log.e("DogDetailFragment", "Error al obtener los detalles del perro.")
                        Toast.makeText(requireContext(), "No se pudieron obtener los detalles del perro", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DogDetailDto>, t: Throwable) {
                    Log.e("DogDetailFragment", "Error al realizar la solicitud", t)
                    Toast.makeText(requireContext(), "Error en la conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Configuramos el botón para cerrar la vista
        binding.btnClose.setOnClickListener {
            // Cierra el fragmento
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Limpiamos el binding cuando la vista se destruya
    }

    companion object {
        private const val DOG_ID = "dog_id"

        fun newInstance(dogId: String): DogDetailFragment {
            val fragment = DogDetailFragment()
            val args = Bundle()
            args.putString(DOG_ID, dogId)
            fragment.arguments = args
            return fragment
        }
    }
}


