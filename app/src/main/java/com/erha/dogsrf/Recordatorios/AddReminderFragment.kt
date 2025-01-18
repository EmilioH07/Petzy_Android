package com.erha.dogsrf.Recordatorios

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.erha.dogsrf.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class AddReminderFragment : Fragment() {

    private lateinit var reminderTypeSpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_reminder, container, false)

        reminderTypeSpinner = view.findViewById(R.id.reminderTypeSpinner)
        dateEditText = view.findViewById(R.id.dateEditText)
        timeEditText = view.findViewById(R.id.timeEditText)
        locationEditText = view.findViewById(R.id.locationEditText)
        notesEditText = view.findViewById(R.id.notesEditText)
        saveButton = view.findViewById(R.id.saveButton)

        setupReminderTypeSpinner()
        setupDateEditText()
        setupTimeEditText()
        setupSaveButton()

        return view
    }

    private fun setupReminderTypeSpinner() {
        val reminderTypes = arrayOf("Consulta", "Vacuna", "Compra de comida", "Otros")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, reminderTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        reminderTypeSpinner.adapter = adapter
    }

    private fun setupDateEditText() {
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    dateEditText.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTimeEditText() {
        timeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    timeEditText.setText(selectedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("Reminders", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val reminderType = reminderTypeSpinner.selectedItem.toString()
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val location = locationEditText.text.toString()
            val notes = notesEditText.text.toString()

            val reminderData = "$reminderType|$date|$time|$location|$notes"
            val reminders = sharedPreferences.getStringSet("reminder_list", mutableSetOf()) ?: mutableSetOf()
            reminders.add(reminderData)

            editor.putStringSet("reminder_list", reminders)
            editor.apply()

            Toast.makeText(requireContext(), "Recordatorio guardado", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}