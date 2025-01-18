package com.erha.dogsrf.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.erha.dogsrf.Main.ui.ViewPagerAdapter
import com.erha.dogsrf.R
import com.erha.dogsrf.Recordatorios.AddReminderDialogFragment
import com.erha.dogsrf.Recordatorios.Reminder
import com.erha.dogsrf.Recordatorios.RemindersFragment
import com.erha.dogsrf.databinding.ActivityMainBinding
import com.erha.dogsrf.ui.fragments.DogsListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), AddReminderDialogFragment.ReminderListener {  // Implementa ReminderListener

    lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el ViewPager2 y el TabLayout
        viewPagerAdapter = ViewPagerAdapter(this) // Usamos FragmentActivity o AppCompatActivity
        binding.viewPager.adapter = viewPagerAdapter

        // Conectar el TabLayout con el ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Asignar títulos y iconos a cada tab
            when (position) {
                0 -> {
                    tab.text = "Avisos"
                    tab.setIcon(R.drawable.bell)
                }
                1 -> {
                    tab.text = "Comida"
                    tab.setIcon(R.drawable.bone)
                }
                2 -> {
                    tab.text = "Razas"
                    tab.setIcon(R.drawable.pets)
                }
                3 -> {
                    tab.text = "Actividad"
                    tab.setIcon(R.drawable.dogwalking)
                }
                4 -> {
                    tab.text = "Adopción"
                    tab.setIcon(R.drawable.collar)
                }
            }
        }.attach()
    }

    // Implementación del método de la interfaz ReminderListener
    override fun onReminderSaved(reminder: Reminder) {
        // Aquí puedes manejar el recordatorio guardado, y luego actualizar el fragmento
        val remindersFragment = supportFragmentManager.findFragmentByTag("f0") as? RemindersFragment
        remindersFragment?.loadRemindersFromPreferences() // Recargar los recordatorios desde SharedPreferences
    }

}




