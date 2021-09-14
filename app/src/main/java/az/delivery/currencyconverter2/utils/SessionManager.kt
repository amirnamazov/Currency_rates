package az.delivery.currencyconverter2.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        pref = context.getSharedPreferences("az.delivery.currencyconverter2.app", Context.MODE_PRIVATE)
        editor = pref.edit()
        editor.apply()
    }

    var lastRequest: Long
        get() = pref.getLong("az.delivery.currencyconverter2.lastRequest", 0)
        set(newName) = editor.putLong("az.delivery.currencyconverter2.lastRequest", newName).apply()
}