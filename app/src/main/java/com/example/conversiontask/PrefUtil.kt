package com.example.conversiontask

import android.content.Context
import android.content.SharedPreferences
import com.example.conversiontask.models.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object PrefUtil {

    private const val SHARED_PREFERENCES_NAME = "PrefUtils.SHARED_PREFERENCES_NAME"
    private const val PREF_CURRENCIES = "PrefUtil.PREF_CURRENCIES"
    private const val PREF_LAST_UPDATE = "PrefUtil.PREF_LAST_UPDATE"

    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private fun getSharedPreferences(): SharedPreferences {
        return preferences
    }

    fun setCurrencies(currencies: MutableList<Currency>) {
        getSharedPreferences().edit().putString(PREF_CURRENCIES, Gson().toJson(currencies)).apply()
    }

    fun getCurrencies(): MutableList<Currency> {
        var list = mutableListOf<Currency>()
        getSharedPreferences().getString(PREF_CURRENCIES, null)?.let { string ->
            list = Gson().fromJson(string, object : TypeToken<MutableList<Currency>>() {}.type)
        }
        return list
    }

    fun setLastUpdate(timestamp: Long) {
        getSharedPreferences().edit().putLong(PREF_LAST_UPDATE, timestamp).apply()
    }

    fun getLastUpdate(): Long {
        return getSharedPreferences().getLong(PREF_LAST_UPDATE, Calendar.getInstance().timeInMillis)
    }

}