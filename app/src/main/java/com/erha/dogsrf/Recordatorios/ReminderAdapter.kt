package com.erha.dogsrf.Recordatorios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erha.dogsrf.R
import com.erha.dogsrf.databinding.ItemReminderBinding

class ReminderAdapter(
    private val reminders: List<Reminder>,
    private val onClick: (Reminder) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.bind(reminder)
    }

    override fun getItemCount(): Int = reminders.size

    inner class ReminderViewHolder(private val binding: ItemReminderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) {
            binding.reminderTypeText.text = reminder.type
            binding.reminderDateText.text = reminder.date

            binding.root.setOnClickListener {
                onClick(reminder)  // Pasa el recordatorio completo
            }
        }
    }
}






