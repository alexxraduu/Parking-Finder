package com.parkingfinder.helper

import android.content.Context

object PrefConfig {
    private const val MY_PREFERENCE_NAME = "com.parkingfinder.helper"
    private const val PREF_EMAIL_KEY = "pref_email_key"
    fun saveEmailInPref(context: Context, email: String?) {
        val pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(PREF_EMAIL_KEY, email)
        editor.apply()
    }

    fun loadEmailFromPref(context: Context): String? {
        val pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return pref.getString(PREF_EMAIL_KEY, "")
    }
}