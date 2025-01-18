package com.erha.dogsrf.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.erha.dogsrf.R
import com.erha.dogsrf.data.remote.model.DogDetailDto
import com.erha.dogsrf.databinding.FragmentDogDetailBinding

class DogDetailDialogFragment : DialogFragment() {

    private var _binding: FragmentDogDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout
        _binding = FragmentDogDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Establece el tamaño del diálogo a pantalla completa
        val dialog = dialog
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,  // Ancho: Ocupa todo el ancho
            ViewGroup.LayoutParams.MATCH_PARENT   // Alto: Ocupa toda la altura
        )

        // Obtener los detalles del perro de los argumentos
        val dogDetails = arguments?.getSerializable("dogDetails") as DogDetailDto

        // Asignar los valores a los TextViews y otros elementos
        binding.apply {
            tvTitle.text = dogDetails.title ?: "Sin información"
            tvSize.text = dogDetails.size ?: "Sin información"
            tvLife.text = dogDetails.lifeexpectancy ?: "Sin información"
            tvTemperament.text = dogDetails.temperament ?: "Sin información"
            tvCoat.text = dogDetails.coattype ?: "Sin información"
            tvExercise.text = dogDetails.exerciseneeds ?: "Sin información"
            tvDaily.text = dogDetails.dailyfood ?: "Sin informacion"

            // Cargar la imagen del perro con Glide
            Glide.with(requireContext())
                .load(dogDetails.image)
                .into(ivImage)
        }

        // Configurar el botón para cerrar el diálogo
        binding.btnClose.setOnClickListener {
            // Asegúrate de que se llama en el hilo principal
            activity?.runOnUiThread {
                dismiss()  // Cierra el DialogFragment
            }
        }
    }

    companion object {
        fun newInstance(dogDetails: DogDetailDto): DogDetailDialogFragment {
            val fragment = DogDetailDialogFragment()
            val args = Bundle()
            args.putSerializable("dogDetails", dogDetails)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar binding cuando se destruya la vista
    }
}

