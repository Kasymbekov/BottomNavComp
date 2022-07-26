package com.bottomnavcomp

import android.content.Context
import android.content.Intent
import android.net.Uri

class Prefs(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveBoardState() {
        preferences.edit().putBoolean("isShown", true).apply()
    }

    fun isShown(): Boolean {
        return preferences.getBoolean("isShown", false)
    }

    fun setString(key: String?, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String?): String? {
        return preferences.getString(key, "")
    }

    //Method to check if it exists or not
    fun checkImage(key: String?): Boolean {
        return preferences.contains(key)
    }

}