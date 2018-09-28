package artground.otterbear.com.artground.main

import android.content.Context
import android.content.SharedPreferences

private const val FILENAME = "prefs"
private const val PREF_TEXT = "Settings"
class AGPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)
    var text: String
        get() = prefs.getString(PREF_TEXT, "")
        set(value) = prefs.edit().putString(PREF_TEXT, value).apply()
}