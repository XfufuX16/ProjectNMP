package com.example.healingyuk.ui.main.add

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.databinding.ActivityAddPlaceBinding
import com.example.healingyuk.util.SessionManager
import org.json.JSONObject

class AddPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPlaceBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // ✅ Set toolbar title & tombol back
        binding.toolbarAddPlace.title = "Tambah Tempat"
        binding.toolbarAddPlace.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val desc = binding.etDescription.text.toString().trim()
            val shortDesc = binding.etShortDesc.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val imageUrl = binding.etImageUrl.text.toString().trim()

            // ✅ Validasi input
            if (name.isEmpty() || desc.isEmpty() || shortDesc.isEmpty() || category.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Kirim data ke API
            val request = object : StringRequest(
                Request.Method.POST,
                ApiService.ADD_PLACE,
                { response ->
                    try {
                        val json = JSONObject(response)
                        if (json.optBoolean("status")) {
                            Toast.makeText(this, "Tempat berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, json.optString("message", "Gagal menambahkan tempat"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Gagal terhubung: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> = hashMapOf(
                    "name" to name,
                    "description" to desc,
                    "short_description" to shortDesc,
                    "category" to category,
                    "image_url" to imageUrl,
                    "user_id" to session.getUserId().toString()
                )
            }

            Volley.newRequestQueue(this).add(request)
        }
    }
}
