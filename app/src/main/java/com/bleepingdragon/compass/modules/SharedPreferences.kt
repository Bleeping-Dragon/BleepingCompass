package com.bleepingdragon.compass.modules

import android.app.Activity
import android.content.Context

class SharedPreferences {

    //Using companion object as equivalent to static to access methods from outside
    //https://stackoverflow.com/questions/40352684/what-is-the-equivalent-of-java-static-methods-in-kotlin
    companion object {

        public fun GetBoolPreference(name: String, activity: Activity): Boolean {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            //Custom defaults
            when (name) {
                "isDetailsActive" -> return sharedPref.getBoolean(name, false)
                else -> {
                    return sharedPref.getBoolean(name, true)
                }
            }
        }


        public fun SetBoolPreference(name: String, setTo: Boolean, activity: Activity) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            //Apply setting using with to the object directly
            //https://www.develou.com/funcion-with-en-kotlin/
            with (sharedPref.edit()) {
                putBoolean(name, setTo)
                apply()
            }
            println("Set preferences")
        }
    }
}