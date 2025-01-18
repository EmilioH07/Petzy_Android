package com.erha.dogsrf.Main.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.erha.dogsrf.Actividades.ActivitiesFragment
import com.erha.dogsrf.Adopcion.AdoptionFragment
import com.erha.dogsrf.Comida.FoodFragment
import com.erha.dogsrf.Recordatorios.RemindersFragment
import com.erha.dogsrf.ui.fragments.DogsListFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // Esta función se llama para obtener el número total de fragmentos
    override fun getItemCount(): Int {
        return 5  // Número total de fragmentos (en tu caso 5)
    }

    // Esta función se llama para obtener el fragmento que debe mostrarse en cada página
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RemindersFragment()  // Fragmento de recordatorios
            1 -> FoodFragment()   // Fragmento de la lista de perritos
            2 -> DogsListFragment()           // Fragmento adicional (Puedes reemplazarlo con el fragmento que quieras)
            3 -> ActivitiesFragment()           // Fragmento adicional (Puedes reemplazarlo con el fragmento que quieras)
            4 -> AdoptionFragment()           // Fragmento adicional (Puedes reemplazarlo con el fragmento que quieras)
            else -> Fragment()        // Por si acaso, siempre devolver un fragmento
        }
    }
}