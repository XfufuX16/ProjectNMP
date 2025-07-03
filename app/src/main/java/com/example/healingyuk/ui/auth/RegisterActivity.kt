package com.example.healingyuk.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.databinding.ActivityRegisterBinding
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val repeatPassword = binding.etRepeatPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(name, email, password, repeatPassword)
        }

        binding.tvToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun registerUser(name: String, email: String, password: String, repeatPassword: String) {
        val request = object : StringRequest(
            Method.POST, ApiService.REGISTER,
            { response ->
                val json = JSONObject(response)
                if (json.optBoolean("status")) {
                    Toast.makeText(this, "Registrasi berhasil. Silakan login.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, json.optString("message", "Gagal registrasi"), Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("REGISTER_ERROR", "Volley: ${error.message}")
                error.networkResponse?.let {
                    Log.e("REGISTER_ERROR", "Status Code: ${it.statusCode}")
                    val responseBody = String(it.data)
                    Log.e("REGISTER_ERROR", "Response Body: $responseBody")
                }

                Toast.makeText(this, "Koneksi gagal: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password,
                    "confirm_password" to repeatPassword // ✅ GANTI INI!
                )
            }
        }

        Volley.newRequestQueue(applicationContext).add(request) // ✅ Gunakan applicationContext
    }

}
