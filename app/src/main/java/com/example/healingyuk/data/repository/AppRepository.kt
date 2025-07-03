package com.example.healingyuk.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AppRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }

    fun cancelAllRequests(tag: Any) {
        requestQueue.cancelAll(tag)
    }
}
