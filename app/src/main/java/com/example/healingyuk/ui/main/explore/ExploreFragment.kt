package com.example.healingyuk.ui.main.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.data.model.Place
import com.example.healingyuk.databinding.FragmentExploreBinding
import com.example.healingyuk.ui.main.add.AddPlaceActivity
import com.example.healingyuk.ui.main.detail.PlaceDetailActivity
import com.android.volley.Request
import org.json.JSONObject

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val placeList = mutableListOf<Place>()
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaceAdapter(placeList) { place ->
            val intent = Intent(requireContext(), PlaceDetailActivity::class.java)
            intent.putExtra("place_id", place.id)
            startActivity(intent)
        }

        binding.rvExplore.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExplore.adapter = adapter

        // Action FAB
        binding.fabAddPlace.setOnClickListener {
            val intent = Intent(requireContext(), AddPlaceActivity::class.java)
            startActivity(intent)
        }

        // Fetch awal
        fetchPlaces()
    }

    // ✅ Auto-refresh saat kembali ke fragment
    override fun onResume() {
        super.onResume()
        fetchPlaces()
    }

    private fun fetchPlaces() {
        val request = StringRequest(
            Request.Method.GET,
            ApiService.GET_PLACES,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.optBoolean("status", false)) {
                        val dataArray = jsonObject.getJSONArray("data")
                        placeList.clear()

                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            placeList.add(
                                Place(
                                    id = obj.optInt("id"),
                                    name = obj.optString("name"),
                                    description = obj.optString("description", ""),
                                    short_description = obj.optString("short_description", ""),
                                    category = obj.optString("category", ""),
                                    image_url = obj.optString("image_url", ""),
                                    latitude = null,
                                    longitude = null,
                                    user_id = obj.optInt("user_id"),
                                    created_at = obj.optString("created_at", "")
                                )
                            )
                        }

                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada data ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("EXPLORE_ERROR", "Parsing error: ${e.message}")
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("EXPLORE_ERROR", "Volley error: ${error.message}")
                Toast.makeText(requireContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
