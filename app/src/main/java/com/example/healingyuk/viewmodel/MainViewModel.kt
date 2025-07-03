package com.example.healingyuk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.MyApp
import com.example.healingyuk.data.api.ApiService
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<JSONObject?>()
    val loginResponse: LiveData<JSONObject?> get() = _loginResponse

    private val _registerResponse = MutableLiveData<JSONObject?>()
    val registerResponse: LiveData<JSONObject?> get() = _registerResponse

    fun login(email: String, password: String) {
        val request = object : StringRequest(
            Request.Method.POST,
            ApiService.LOGIN,
            { response ->
                Log.d("LOGIN_RESPONSE", response)
                _loginResponse.value = JSONObject(response)
            },
            { error ->
                Log.e("LOGIN_ERROR", error.toString())
                _loginResponse.value = null
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "email" to email,
                    "password" to password
                )
            }
        }

        Volley.newRequestQueue(MyApp.instance).add(request)
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        val request = object : StringRequest(
            Request.Method.POST,
            ApiService.REGISTER,
            { response ->
                Log.d("REGISTER_RESPONSE", response)
                _registerResponse.value = JSONObject(response)
            },
            { error ->
                Log.e("REGISTER_ERROR", "Volley error: ${error.message}")

                error.networkResponse?.let {
                    Log.e("REGISTER_ERROR", "HTTP Status Code: ${it.statusCode}")
                    val responseData = String(it.data ?: ByteArray(0))
                    Log.e("REGISTER_ERROR", "Response body: $responseData")
                }

                _registerResponse.value = null
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password,
                    "confirm_password" to confirmPassword
                )
                Log.d("REGISTER_PARAMS", params.toString())
                return params
            }
        }

        Volley.newRequestQueue(MyApp.instance).add(request)
    }
}
