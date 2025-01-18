package com.erha.dogsrf.Comida

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.erha.dogsrf.R
import com.erha.dogsrf.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    private var totalFoodInKg: Double = 0.0
    private var foodAmountInGrams: Double = 0.0
    private var initialFoodAmountInGrams: Double = 0.0

    private lateinit var foodProgressBar: ProgressBar
    private lateinit var totalFoodLabel: TextView
    private lateinit var foodToFeedEditText: EditText
    private lateinit var totalFoodAmountLabel: TextView
    private lateinit var feedDogButton: Button
    private lateinit var addFoodButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food, container, false)

        // Inicializar vistas
        foodProgressBar = view.findViewById(R.id.foodProgressBar)
        totalFoodLabel = view.findViewById(R.id.totalFoodLabel)
        foodToFeedEditText = view.findViewById(R.id.foodToFeedEditText)
        totalFoodAmountLabel = view.findViewById(R.id.totalFoodAmountLabel)
        feedDogButton = view.findViewById(R.id.feedDogButton)
        addFoodButton = view.findViewById(R.id.addFoodButton)

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("FoodData", 0)

        // Cargar datos y actualizar la UI
        loadFoodData()
        updateProgressBarAndLabel()

        // Configurar acciones de botones
        feedDogButton.setOnClickListener { feedDog() }
        addFoodButton.setOnClickListener { addFood() }

        // Configurar para ocultar el teclado al hacer tap fuera del campo de texto
        setupHideKeyboardOnTap(view)

        return view
    }

    private fun setupHideKeyboardOnTap(view: View) {
        view.setOnTouchListener { _, _ ->
            view.clearFocus()
            true
        }
    }

    // Cargar datos desde SharedPreferences
    private fun loadFoodData() {
        foodAmountInGrams = sharedPreferences.getFloat("foodAmountInGrams", 0f).toDouble()
        initialFoodAmountInGrams = sharedPreferences.getFloat("initialFoodAmountInGrams", 0f).toDouble()

        if (initialFoodAmountInGrams == 0.0 && foodAmountInGrams == 0.0) {
            initialFoodAmountInGrams = 0000.0
            foodAmountInGrams = initialFoodAmountInGrams
            saveFoodData()
        }

        updateProgressBarAndLabel()
    }

    // Guardar datos en SharedPreferences
    private fun saveFoodData() {
        sharedPreferences.edit {
            putFloat("foodAmountInGrams", foodAmountInGrams.toFloat())
            putFloat("initialFoodAmountInGrams", initialFoodAmountInGrams.toFloat())
        }
    }

    private fun addFood() {
        val inputEditText = EditText(requireContext()).apply {
            hint = "Cantidad en kg"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Agregar comida")
            .setMessage("Ingresa la cantidad de comida total en kilogramos:")
            .setView(inputEditText)
            .setPositiveButton("Agregar") { _, _ ->
                val input = inputEditText.text.toString()
                val foodInKg = input.toDoubleOrNull()

                if (foodInKg != null && foodInKg > 0) {
                    val foodInGrams = foodInKg * 1000

                    // Incrementar la comida actual
                    foodAmountInGrams += foodInGrams

                    // Ahora establecer el nuevo total como el 100%
                    initialFoodAmountInGrams = foodAmountInGrams

                    saveFoodData()
                    updateProgressBarAndLabel()
                } else {
                    Toast.makeText(requireContext(), "Por favor, introduce una cantidad válida.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        alertDialog.show()
    }



    private fun feedDog() {
        val foodToFeedInGrams = foodToFeedEditText.text.toString().toDoubleOrNull()

        if (foodToFeedInGrams == null || foodToFeedInGrams <= 0) {
            Toast.makeText(requireContext(), "Por favor, introduce una cantidad válida.", Toast.LENGTH_SHORT).show()
            return
        }

        if (foodToFeedInGrams > foodAmountInGrams) {
            AlertDialog.Builder(requireContext())
                .setTitle("¡Comida insuficiente!")
                .setMessage("No tienes suficiente comida para alimentar al perro. Intenta con una cantidad menor.")
                .setPositiveButton("OK", null)
                .create()
                .show()
            return
        }

        foodAmountInGrams -= foodToFeedInGrams
        saveFoodData()
        updateProgressBarAndLabel()
        foodToFeedEditText.text.clear()
    }

    private fun updateProgressBarAndLabel() {
        if (initialFoodAmountInGrams <= 0.0) {
            foodProgressBar.progress = 0
            totalFoodLabel.text = "Total de comida: 0.00 kg"
            totalFoodAmountLabel.text = "0.00"
            return
        }

        val percentage = foodAmountInGrams / initialFoodAmountInGrams
        foodProgressBar.progress = (percentage * 100).toInt()

        val remainingFoodInKg = foodAmountInGrams / 1000
        val totalFoodInKg = initialFoodAmountInGrams / 1000

        totalFoodLabel.text = "Total de comida: %.2f kg".format(remainingFoodInKg)
        totalFoodAmountLabel.text = "%.2f".format(totalFoodInKg)
    }
}