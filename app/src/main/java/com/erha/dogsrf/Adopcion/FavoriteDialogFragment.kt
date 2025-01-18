package com.erha.dogsrf.Adopcion

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erha.dogsrf.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogsAdoptionAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var api: DogService

    companion object {
        fun newInstance(sharedPreferencesManager: SharedPreferencesManager): FavoriteDialogFragment {
            val fragment = FavoriteDialogFragment()
            val args = Bundle()
            args.putSerializable("sharedPreferencesManager", sharedPreferencesManager)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sharedPreferencesManager = it.getSerializable("sharedPreferencesManager") as? SharedPreferencesManager
                ?: throw IllegalArgumentException("SharedPreferencesManager es nulo o inválido")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvFavoriteDogs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DogsAdoptionAdapter(emptyList(), sharedPreferencesManager)
        recyclerView.adapter = adapter

        val btnClose = view.findViewById<Button>(R.id.btnCloseFavoriteDialog)
        btnClose.setOnClickListener {
            dismiss() // Cierra el diálogo
        }

        api = RetrofitInstance.api
        fetchFavoriteDogs()
    }

    private fun fetchFavoriteDogs() {
        api.getDogsList().enqueue(object : Callback<List<DogAdoption>> {
            override fun onResponse(call: Call<List<DogAdoption>>, response: Response<List<DogAdoption>>) {
                if (response.isSuccessful) {
                    val dogList = response.body() ?: emptyList()
                    val favoriteDogs = dogList.filter { dog ->
                        sharedPreferencesManager.isFavorite(dog.id)
                    }
                    adapter.updateData(favoriteDogs)
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DogAdoption>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}




