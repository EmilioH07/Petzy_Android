package com.erha.dogsrf.Actividades

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erha.dogsrf.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ActivitiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var secondRecyclerView: RecyclerView
    private lateinit var adapter: ActivityAdapter
    private lateinit var secondAdapter: ActivityAdapter
    private val activities = mutableListOf<Activity>()
    private val secondActivities = mutableListOf<Activity>()
    private lateinit var activityService: ActivityService
    private lateinit var secondActivityService: ActivityService // Nuevo servicio para la segunda API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)

        // Configuración del primer RecyclerView
        recyclerView = view.findViewById(R.id.activityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = ActivityAdapter(activities) { activity -> openDetails(activity) }
        recyclerView.adapter = adapter

        // Configuración del segundo RecyclerView
        secondRecyclerView = view.findViewById(R.id.secondActivityRecyclerView)
        secondRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        secondAdapter = ActivityAdapter(secondActivities) { activity -> openDetails(activity) }
        secondRecyclerView.adapter = secondAdapter

        // Configuración de Retrofit para la primera API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://private-bb2ffb-actividades2.apiary-mock.com/") // URL primera API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        activityService = retrofit.create(ActivityService::class.java)

        // Configuración de Retrofit para la segunda API
        val secondRetrofit = Retrofit.Builder()
            .baseUrl("https://private-324614-actividades21.apiary-mock.com/") // URL segunda API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        secondActivityService = secondRetrofit.create(ActivityService::class.java)

        // Cargar actividades desde ambas APIs
        loadActivities()
        loadSecondActivities()

        return view
    }

    private fun loadActivities() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Llamada a la primera API
                val response = activityService.getWalkPlaces()

                // Log para ver la respuesta cruda antes de intentar parsearla
                Log.d("ActivitiesFragment", "Raw Response: $response")

                activities.clear()
                activities.addAll(response)
                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e("ActivitiesFragment", "Error fetching activities", e)  // Log de error
            }
        }
    }

    private fun loadSecondActivities() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Llamada a la segunda API con el nuevo endpoint
                val response = secondActivityService.getPlazaPlaces() // Aquí utilizamos el mismo método de la primera API, ya que se espera una estructura similar

                // Log para ver la respuesta cruda antes de intentar parsearla
                Log.d("ActivitiesFragment", "Raw Response (Second API): $response")

                secondActivities.clear()
                secondActivities.addAll(response)
                secondAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e("ActivitiesFragment", "Error fetching second activities", e)  // Log de error
            }
        }
    }

    private fun openDetails(activity: Activity) {
        val bundle = Bundle()

        // Llamada a la API para obtener los detalles de la actividad
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Verificar si la actividad pertenece al primer o segundo RecyclerView
                val detailsResponse = if (activities.contains(activity)) {
                    activityService.getActivityDetails(activity.id)  // Primer API
                } else {
                    secondActivityService.getPlazaActivityDetails(activity.id)  // Segunda API
                }

                // Log para ver la respuesta
                Log.d("ActivitiesFragment", "Activity Details Response: $detailsResponse")

                // Asegúrate de que los valores de latitud y longitud son correctos
                val latitud = detailsResponse.latitud
                val longitud = detailsResponse.longitud
                Log.d("ActivitiesFragment", "Latitud: $latitud, Longitud: $longitud")

                // Agregar los detalles de latitud y longitud al bundle
                bundle.putDouble("LATITUD", latitud)
                bundle.putDouble("LONGITUD", longitud)
                bundle.putString("TITLE", detailsResponse.title)

                // Crear y mostrar el MapDialogFragment
                val mapDialogFragment = MapDialogFragment.newInstance(latitud, longitud, detailsResponse.title)
                mapDialogFragment.show(parentFragmentManager, "MapDialog")

            } catch (e: Exception) {
                Log.e("ActivitiesFragment", "Error fetching activity details", e)
            }
        }
    }


}






