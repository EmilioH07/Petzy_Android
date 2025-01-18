package com.erha.dogsrf.Recordatorios

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.erha.dogsrf.R
import com.erha.dogsrf.databinding.FragmentReminderDetailBinding

class ReminderDetailFragment : DialogFragment() {

    private lateinit var binding: FragmentReminderDetailBinding

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.8).toInt()
        val height = (displayMetrics.heightPixels * 0.6).toInt()
        dialog?.window?.setLayout(width, height)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderDetailBinding.inflate(inflater, container, false)

        // Recuperar los datos del Bundle
        val type = arguments?.getString("type")
        val date = arguments?.getString("date")
        val time = arguments?.getString("time")
        val location = arguments?.getString("location")
        val notes = arguments?.getString("notes")

        // Mostrar los datos en las vistas correspondientes
        binding.typeTextView.text = type
        binding.dateTextView.text = date
        binding.timeTextView.text = time
        binding.locationTextView.text = location
        binding.notesTextView.text = notes

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // Establecer el fondo del diálogo
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)
        return dialog
    }

}

