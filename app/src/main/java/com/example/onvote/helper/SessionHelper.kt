package com.example.onvote.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class Session(context: Context) {
    private val sp: SharedPreferences = context.getSharedPreferences("com.onvote.login", Context.MODE_PRIVATE)
    private var userID: Int = 0

    fun setUserID(userID: Int) {
        sp.edit().putInt("userID", userID).apply() // save userID in shared preferences
        Log.d("UserID", userID.toString())
    }

    fun getUserID(): Int {
        userID = sp.getInt("userID", 0) // default value 0 (none)
        return userID
    }
}