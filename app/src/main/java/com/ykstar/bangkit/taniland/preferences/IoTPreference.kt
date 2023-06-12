package com.ykstar.bangkit.taniland.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IoTPreference(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(IOT_PREF, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveIot(lahanId: String, iotId: String): Boolean {
        val iotMap: MutableMap<String, String> = getIotMap()
        return if (!iotMap.containsKey(lahanId)) {
            iotMap[lahanId] = iotId
            prefs.edit().putString(IOT_MAP, gson.toJson(iotMap)).apply()
            true
        } else {
            false
        }
    }

    fun getIot(lahanId: String): String? {
        val iotMap: Map<String, String> = getIotMap()
        return iotMap[lahanId]
    }

    fun deleteIot(lahanId: String) {
        val iotMap: MutableMap<String, String> = getIotMap()
        iotMap.remove(lahanId)
        prefs.edit().putString(IOT_MAP, gson.toJson(iotMap)).apply()
    }

    private fun getIotMap(): MutableMap<String, String> {
        val iotJson = prefs.getString(IOT_MAP, "")
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(iotJson, type) ?: mutableMapOf()
    }

    companion object {
        private const val IOT_MAP = "iot_map"
        private const val IOT_PREF = "IoTPreferences"
    }
}
