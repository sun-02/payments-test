package com.example.payments_test.data

import android.content.SharedPreferences

class SessionManager(
    private val sharedPrefs: SharedPreferences
) {
    var authToken: String? = null
        get() = field ?: getToken()

    fun saveToken(newToken: String?, async: Boolean = false) {
        authToken = newToken
        sharedPrefs.edit().apply {
            putString(AUTH_TOKEN, authToken)
            if (async) {
                apply()
            } else {
                commit()
            }
        }
    }

    fun getToken() = sharedPrefs.getString(AUTH_TOKEN, null)

    companion object {
        private const val AUTH_TOKEN = "AuthToken"
    }
}