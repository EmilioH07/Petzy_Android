package com.erha.dogsrf.Adopcion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erha.dogsrf.R
import com.squareup.picasso.Picasso
import java.net.URLEncoder

class DogsAdoptionAdapter(
    private var dogList: List<DogAdoption>,
    private val sharedPreferencesManager: SharedPreferencesManager
) : RecyclerView.Adapter<DogsAdoptionAdapter.DogViewHolder>() {

    fun updateData(newData: List<DogAdoption>) {
        dogList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dogadoption_element, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogList[position]

        // Bind data to your views
        holder.tvDogName.text = dog.nombre
        holder.tvDogLocation.text = dog.ubicacion
        holder.tvDogAge.text = dog.edad
        Glide.with(holder.itemView.context)
            .load(dog.image)
            .into(holder.ivDogImage)

        // Verificar si es favorito usando SharedPreferences
        val isFavorite = sharedPreferencesManager.isFavorite(dog.id)
        setLikeButtonState(holder, isFavorite)

        // Acción del botón de "Like"
        holder.btnLike.setOnClickListener {
            val newState = !isFavorite
            sharedPreferencesManager.saveFavorite(dog.id, newState)
            setLikeButtonState(holder, newState)
        }

        // Acción del botón de WhatsApp
        holder.btnWhatsApp.setOnClickListener {
            val phoneNumber = dog.cel
            if (phoneNumber.isNotEmpty()) {
                showWhatsAppDialog(holder.itemView.context, phoneNumber)
            }
        }
    }

    private fun showWhatsAppDialog(context: Context, phoneNumber: String) {
        AlertDialog.Builder(context)
            .setMessage("¿Quieres comunicarte con el responsable de este perrito?")
            .setPositiveButton("Sí") { _, _ ->
                openWhatsApp(context, phoneNumber)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun openWhatsApp(context: Context, phoneNumber: String) {
        val message = "¡Hola! Estoy interesado en adoptar este perrito."
        val url = "https://wa.me/$phoneNumber?text=${URLEncoder.encode(message, "UTF-8")}"

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getItemCount(): Int = dogList.size

    private fun setLikeButtonState(holder: DogViewHolder, isFavorite: Boolean) {
        val drawableRes = if (isFavorite) R.drawable.heart_active else R.drawable.heart
        holder.btnLike.setImageResource(drawableRes)
    }

    inner class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivDogImage: ImageView = view.findViewById(R.id.ivDogImage)
        val btnLike: ImageButton = view.findViewById(R.id.btnLike)
        val btnWhatsApp: ImageButton = view.findViewById(R.id.btnWhatsApp)
        val tvDogName: TextView = view.findViewById(R.id.tvDogName)
        val tvDogLocation: TextView = view.findViewById(R.id.tvDogLocation)
        val tvDogAge: TextView = view.findViewById(R.id.tvDogAge)
    }
}






