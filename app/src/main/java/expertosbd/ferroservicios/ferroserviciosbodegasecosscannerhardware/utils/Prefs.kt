package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val PREFS_FILENAME = "expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils.Prefs"
    private val USER = "user"
    private val FIRST_NAME = "first_name"
    private val LAST_NAME = "last_name"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var user: String
        get() = prefs.getString(USER, "")
        set(value) = prefs.edit().putString(USER, value).apply()
    var firstName: String
        get() = prefs.getString(FIRST_NAME, "")
        set(value) = prefs.edit().putString(FIRST_NAME, value).apply()
    var lastName: String
        get() = prefs.getString(LAST_NAME, "")
        set(value) = prefs.edit().putString(LAST_NAME, value).apply()
}