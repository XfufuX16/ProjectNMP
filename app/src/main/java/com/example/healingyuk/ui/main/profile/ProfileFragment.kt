package com.example.healingyuk.ui.main.profile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.databinding.FragmentProfileBinding
import com.example.healingyuk.util.SessionManager
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        session = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchProfileData()
    }

    private fun fetchProfileData() {
        val request = object : StringRequest(
            Request.Method.POST,
            ApiService.PROFILE,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.optBoolean("status", false) && json.has("data")) {
                        val obj = json.getJSONObject("data")

                        binding.tvName.text = obj.optString("name", "-")
                        binding.tvEmail.text = obj.optString("email", "-")
                        binding.tvJoined.text = "Join on ${obj.optString("created_at", "-")}"
                        binding.tvTotalFav.text = "Favorite: ${obj.optInt("total_favorites", 0)}"
                    } else {
                        Toast.makeText(requireContext(), "Gagal memuat data profil", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Gagal koneksi ke server", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("user_id" to session.getUserId().toString())
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
