package com.example.healingyuk.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "healingyuk_pref"
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val TOKEN = "token"
        private const val IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUserSession(id: Int, name: String, email: String, token: String) {
        prefs.edit().apply {
            putInt(USER_ID, id)
            putString(USER_NAME, name)
            putString(USER_EMAIL, email)
            putString(TOKEN, token)
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): Int = prefs.getInt(USER_ID, -1)

    fun getUserName(): String = prefs.getString(USER_NAME, "") ?: ""

    fun getUserEmail(): String = prefs.getString(USER_EMAIL, "") ?: ""

    fun getToken(): String = prefs.getString(TOKEN, "") ?: ""

    fun isLoggedIn(): Boolean = prefs.getBoolean(IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
