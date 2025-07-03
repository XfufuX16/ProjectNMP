package com.example.healingyuk.ui.main.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.databinding.ActivityPlaceDetailBinding
import com.example.healingyuk.util.SessionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailBinding
    private lateinit var session: SessionManager
    private var placeId: Int = -1
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Setup Toolbar
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Tempat"

        session = SessionManager(this)
        placeId = intent.getIntExtra("place_id", -1)

        if (placeId == -1) {
            Toast.makeText(this, "Tempat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        getPlaceDetail()

        binding.btnFav.setOnClickListener {
            if (isFavorite) removeFavorite() else addFavorite()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getPlaceDetail() {
        val url = "${ApiService.GET_PLACE_BY_ID}?id=$placeId"
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    val obj = JSONObject(response).getJSONObject("data")
                    binding.tvName.text = obj.getString("name")
                    binding.tvCategory.text = obj.getString("category")
                    binding.tvDescription.text = obj.getString("description")
                    Picasso.get().load(obj.getString("image_url")).into(binding.ivPlace)

                    checkFavorite()
                } catch (e: Exception) {
                    Toast.makeText(this, "Data tidak valid", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Toast.makeText(this, "Gagal ambil data", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun checkFavorite() {
        val userId = session.getUserId()
        val url = "${ApiService.GET_FAVORITES}?user_id=$userId"
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val array = JSONObject(response).optJSONArray("data") ?: return@StringRequest
                isFavorite = false
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    if (obj.getInt("id") == placeId) {
                        isFavorite = true
                        break
                    }
                }
                updateFavoriteButton()
            },
            {
                updateFavoriteButton()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun updateFavoriteButton() {
        binding.btnFav.text = if (isFavorite) "Remove Favorite" else "Add to Favorite"
    }

    private fun addFavorite() {
        val request = object : StringRequest(
            Request.Method.POST,
            ApiService.ADD_FAVORITE,
            {
                Toast.makeText(this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                isFavorite = true
                updateFavoriteButton()
            },
            {
                Toast.makeText(this, "Gagal tambah favorit", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> =
                hashMapOf(
                    "user_id" to session.getUserId().toString(),
                    "place_id" to placeId.toString()
                )
        }
        Volley.newRequestQueue(this).add(request)
    }

    private fun removeFavorite() {
        val request = object : StringRequest(
            Request.Method.POST,
            ApiService.REMOVE_FAVORITE,
            {
                Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()
                isFavorite = false
                updateFavoriteButton()
            },
            {
                Toast.makeText(this, "Gagal hapus favorit", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> =
                hashMapOf(
                    "user_id" to session.getUserId().toString(),
                    "place_id" to placeId.toString()
                )
        }
        Volley.newRequestQueue(this).add(request)
    }
}
