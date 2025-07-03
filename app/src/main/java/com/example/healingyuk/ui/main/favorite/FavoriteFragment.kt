package com.example.healingyuk.ui.main.favorite

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.data.model.Place
import com.example.healingyuk.databinding.FragmentFavoriteBinding
import com.example.healingyuk.ui.main.detail.PlaceDetailActivity
import com.example.healingyuk.ui.main.explore.PlaceAdapter
import com.example.healingyuk.util.SessionManager
import org.json.JSONObject

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteList = mutableListOf<Place>()
    private lateinit var adapter: PlaceAdapter
    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        session = SessionManager(requireContext())

        adapter = PlaceAdapter(favoriteList) { place ->
            val intent = Intent(requireContext(), PlaceDetailActivity::class.java)
            intent.putExtra("place_id", place.id)
            startActivity(intent)
        }

        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.adapter = adapter

        fetchFavorites()
    }

    override fun onResume() {
        super.onResume()
        fetchFavorites() // Refresh data favorit setiap kali kembali ke fragment ini
    }

    private fun fetchFavorites() {
        val url = "${ApiService.GET_FAVORITES}?user_id=${session.getUserId()}"

        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.optBoolean("status") && json.has("data")) {
                        val dataArray = json.optJSONArray("data")
                        favoriteList.clear()

                        if (dataArray != null) {
                            for (i in 0 until dataArray.length()) {
                                val obj = dataArray.getJSONObject(i)
                                favoriteList.add(
                                    Place(
                                        id = obj.optInt("id"),
                                        name = obj.optString("name"),
                                        description = obj.optString("description", ""),
                                        short_description = obj.optString("short_description", ""),
                                        category = obj.optString("category", ""),
                                        image_url = obj.optString("image_url", ""),
                                        latitude = null,
                                        longitude = null,
                                        user_id = obj.optInt("user_id", 0),
                                        created_at = obj.optString("created_at", "")
                                    )
                                )
                            }
                        }

                        adapter.notifyDataSetChanged()
                        if (favoriteList.isEmpty()) {
                            Toast.makeText(requireContext(), "Tidak ada data favorit", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), json.optString("message", "Gagal memuat favorit"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Terjadi kesalahan parsing", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Koneksi ke server gagal", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
