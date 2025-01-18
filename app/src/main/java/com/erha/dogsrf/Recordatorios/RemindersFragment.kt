package com.erha.dogsrf.Recordatorios

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erha.dogsrf.R
import com.erha.dogsrf.databinding.FragmentRemindersBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erha.dogsrf.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RemindersFragment : Fragment(), AddReminderDialogFragment.ReminderListener {

    private lateinit var binding: FragmentRemindersBinding
    private lateinit var adapter: ReminderAdapter
    private var reminderList: MutableList<Reminder> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRemindersFromPreferences() // Cargar los recordatorios al crear el fragmento
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemindersBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        setupRecyclerView()

        // Cargar los recordatorios desde SharedPreferences
        loadRemindersFromPreferences()

        // Configurar botón para agregar recordatorio
        binding.btnAddReminder.setOnClickListener {
            // Mostrar el AddReminderDialogFragment
            val addReminderDialog = AddReminderDialogFragment()
            addReminderDialog.setReminderListener(this) // Establecer el listener
            addReminderDialog.show(childFragmentManager, "AddReminderDialog")
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ReminderAdapter(reminderList) { reminder ->
            // Lógica cuando se hace clic en un recordatorio
            showReminderDetails(reminder)
        }

        binding.recyclerViewReminders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReminders.adapter = adapter

        // Configurar deslizar para eliminar
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No implementaremos mover elementos
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedReminder = reminderList[position]

                // Eliminar el recordatorio de la lista
                reminderList.removeAt(position)
                adapter.notifyItemRemoved(position)

                // Actualizar SharedPreferences
                saveRemindersToPreferences()

                // Mostrar mensaje de confirmación
                Snackbar.make(binding.recyclerViewReminders, "Recordatorio eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") {
                        // Restaurar el recordatorio si se elige deshacer
                        reminderList.add(position, removedReminder)
                        adapter.notifyItemInserted(position)
                        saveRemindersToPreferences()
                    }
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewReminders)
    }

    private fun showReminderDetails(reminder: Reminder) {
        // Crear una nueva instancia del fragmento de detalles
        val dialogFragment = ReminderDetailFragment().apply {
            arguments = Bundle().apply {
                putString("type", reminder.type)
                putString("date", reminder.date)
                putString("time", reminder.time)
                putString("location", reminder.location)
                putString("notes", reminder.notes)
            }
        }
        // Mostrar el dialog fragment
        dialogFragment.show(childFragmentManager, "ReminderDetailDialog")
    }

    // Método para recargar los recordatorios desde SharedPreferences
    fun loadRemindersFromPreferences() {
        val sharedPref = activity?.getSharedPreferences("remindersPref", Context.MODE_PRIVATE)
        val json = sharedPref?.getString("remindersList", null)

        if (json != null) {
            val type = object : TypeToken<List<Reminder>>() {}.type
            val reminders = Gson().fromJson<List<Reminder>>(json, type)
            reminderList.clear()  // Limpiar la lista antes de añadir los nuevos
            reminderList.addAll(reminders)  // Añadir todos los recordatorios
            adapter.notifyDataSetChanged()  // Notificar al adaptador que los datos han cambiado
        }
    }

    // Método para guardar la lista de recordatorios en SharedPreferences
    private fun saveRemindersToPreferences() {
        val sharedPref = activity?.getSharedPreferences("remindersPref", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        val json = Gson().toJson(reminderList)
        editor?.putString("remindersList", json)
        editor?.apply()
    }

    // Implementación del método que se llama cuando se guarda un recordatorio
    override fun onReminderSaved(reminder: Reminder) {
        // Agregar el nuevo recordatorio a la lista
        reminderList.add(reminder)
        // Notificar al adaptador que los datos han cambiado
        adapter.notifyItemInserted(reminderList.size - 1)  // Insertar el nuevo item en la posición correcta

        // Guardar en SharedPreferences
        saveRemindersToPreferences()
    }
}













