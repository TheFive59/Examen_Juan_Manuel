package jmmh.bluelabs.m513.ui

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager



class SharedPreferencesUtil() {
    fun getDefaultsPreference(key: String?, context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, null)
    }

    fun setDefaultsPreference(key: String?, value: String?, context: Context?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

}