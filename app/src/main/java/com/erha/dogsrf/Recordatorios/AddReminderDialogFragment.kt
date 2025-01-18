package com.erha.dogsrf.Recordatorios

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.erha.dogsrf.databinding.FragmentAddReminderBinding
import com.google.gson.Gson
import java.util.Calendar

class AddReminderDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddReminderBinding
    private var listener: ReminderListener? = null

    interface ReminderListener {
        fun onReminderSaved(reminder: Reminder)
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.8).toInt()  // 80% del ancho de la pantalla
        val height = (displayMetrics.heightPixels * 0.6).toInt()  // 60% de la altura de la pantalla
        dialog?.window?.setLayout(width, height)
    }



    // Método para establecer el listener
    fun setReminderListener(listener: ReminderListener) {
        this.listener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReminderListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ReminderListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReminderBinding.inflate(inflater, container, false)

        // Configurar el Spinner para tipo de recordatorio
        val reminderTypes = arrayOf("Consulta veterinaria", "Compra de comida", "Vacuna", "Corte de pelo", "Otro")
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, reminderTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reminderTypeSpinner.adapter = spinnerAdapter

        // Configurar botón para seleccionar la fecha
        binding.dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // Configurar botón para seleccionar la hora
        binding.timeEditText.setOnClickListener {
            showTimePickerDialog()
        }

        // Configurar el botón para guardar el recordatorio
        binding.saveButton.setOnClickListener {
            saveReminder()
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Mostrar la fecha seleccionada en el EditText
                binding.dateEditText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Mostrar la hora seleccionada en el EditText
                binding.timeEditText.setText("$selectedHour:$selectedMinute")
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun saveReminder() {
        val reminderType = binding.reminderTypeSpinner.selectedItem.toString()
        val date = binding.dateEditText.text.toString()
        val time = binding.timeEditText.text.toString()
        val location = binding.locationEditText.text.toString()
        val notes = binding.notesEditText.text.toString()

        // Crear el recordatorio
        val reminder = Reminder(reminderType, date, time, location, notes)

        // Guardar el recordatorio en SharedPreferences
        val sharedPref = activity?.getSharedPreferences("remindersPref", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        val reminderJson = Gson().toJson(reminder)

        // Guardar recordatorio como JSON
        val remindersJson = sharedPref?.getString("remindersList", "[]") ?: "[]"
        val reminders = Gson().fromJson(remindersJson, Array<Reminder>::class.java).toMutableList()
        reminders.add(reminder)
        editor?.putString("remindersList", Gson().toJson(reminders))
        editor?.apply()

        // Notificar al fragmento principal que se ha guardado un recordatorio
        listener?.onReminderSaved(reminder)

        // Cerrar el fragmento
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Establecer el fondo del diálogo
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)
        return dialog
    }

}




