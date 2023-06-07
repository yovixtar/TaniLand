package com.ykstar.bangkit.taniland.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreference(context: Context) {

    private val PREF_NAME = "user_pref"
    private val USER_ID = "user_id"
    private val TOKEN = "user_token"

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = sharedPref.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPref.getString(TOKEN, null)
    }

    fun removeToken() {
        val editor = sharedPref.edit()
        editor.remove(TOKEN)
        editor.apply()
    }

    fun saveUserID(user_id: String) {
        val editor = sharedPref.edit()
        editor.putString(USER_ID, user_id)
        editor.apply()
    }

    fun getUserID(): String? {
        return sharedPref.getString(USER_ID, null)
    }

    fun removeUserID() {
        val editor = sharedPref.edit()
        editor.remove(USER_ID)
        editor.apply()
    }
}
