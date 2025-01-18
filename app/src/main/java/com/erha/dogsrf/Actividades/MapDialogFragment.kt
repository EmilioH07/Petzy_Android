package com.erha.dogsrf.Actividades

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.erha.dogsrf.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(latitud: Double, longitud: Double, title: String): MapDialogFragment {
            val fragment = MapDialogFragment()
            val bundle = Bundle()
            bundle.putDouble("LATITUD", latitud)
            bundle.putDouble("LONGITUD", longitud)
            bundle.putString("TITLE", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Inicializar MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Obtener latitud, longitud y título desde los argumentos
        val latitud = arguments?.getDouble("LATITUD") ?: 0.0
        val longitud = arguments?.getDouble("LONGITUD") ?: 0.0
        val title = arguments?.getString("TITLE") ?: "Ubicación desconocida"

        // Log para depurar los valores de latitud y longitud
        Log.d("MapDialogFragment", "Latitud: $latitud, Longitud: $longitud")

        // Configurar el mapa
        mapView.getMapAsync { googleMap ->
            if (latitud != 0.0 && longitud != 0.0) {
                val location = LatLng(latitud, longitud)
                googleMap.addMarker(MarkerOptions().position(location).title(title))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
            }

            val locationTitleTextView: TextView = view.findViewById(R.id.locationTitle)
            locationTitleTextView.text = title

            // Manejo del botón "Abrir en Google Maps"
            val openMapButton: Button = view.findViewById(R.id.openMapButton)
            openMapButton.setOnClickListener {
                openGoogleMaps(latitud, longitud)
            }

            // Manejo del botón "Cerrar" (ImageButton)
            val closeButton: ImageButton = view.findViewById(R.id.closeButton)
            closeButton.setOnClickListener {
                dismiss()  // Cierra el DialogFragment
            }
        }

        return view
    }

    // Método que se llama después de que la vista haya sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el tamaño del diálogo para que ocupe toda la pantalla
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,  // Ancho de la ventana (ocupará todo el ancho)
            ViewGroup.LayoutParams.MATCH_PARENT   // Alto de la ventana (ocupará todo el alto)
        )
    }

    private fun openGoogleMaps(latitud: Double, longitud: Double) {
        val uri = Uri.parse("geo:$latitud,$longitud?q=$latitud,$longitud") // URI para ver ubicación con marcador y barra de búsqueda
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps") // Asegúrate de que Google Maps esté disponible
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Log.e("MapDialogFragment", "Google Maps no está instalado.")
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}






