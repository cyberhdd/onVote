package com.example.onvote.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DBNAME, null, 1){

    private val sp: SharedPreferences

    companion object {
        private const val TAG = "DatabaseHelper"
        const val DBNAME = "onVoteDev"
    }

    init {
        sp = context.getSharedPreferences("com.onvote.login", Context.MODE_PRIVATE)
    }

    override fun onCreate(myDB: SQLiteDatabase) {
        myDB.execSQL(
            "CREATE TABLE USERS(UserID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, email TEXT, password TEXT, userGoal INTEGER DEFAULT 53)"
        )
    }

    override fun onUpgrade(myDB: SQLiteDatabase, i: Int, i1: Int) {
        myDB.execSQL("DROP TABLE IF EXISTS USERS")
        onCreate(myDB)
    }

    fun insertData(username: String, email: String, password: String): Boolean {
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("email", email)
        contentValues.put("password", password)
        val result = myDB.insert("users", null, contentValues)

        return result != -1L
    }

    fun checkUsername(username: String): Boolean {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE username = ?",
            arrayOf(username)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun checkUsernamePassword(username: String, password: String): Int {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE username = ? and password = ?",
            arrayOf(username, password)
        )

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val userID = cursor.getInt(0)
            cursor.close()
            Log.d(TAG, "checkUsernamePassword: $userID")
            return userID
        } else {
            cursor.close()
            return -1
        }
    }


/*
    @SuppressLint("Range")
    fun getUser(UserID: Int): UserModel? {
        val db = this.readableDatabase
        var userModel: UserModel? = null
        val query = "SELECT * FROM users WHERE UserID = ?"
        val userCursor = db.rawQuery(query, arrayOf(UserID.toString()))

        if (userCursor.moveToFirst()) {
            userModel = UserModel(
                userCursor.getInt(userCursor.getColumnIndex("UserID")),
                userCursor.getString(userCursor.getColumnIndex("username")),
                userCursor.getString(userCursor.getColumnIndex("email")),
                userCursor.getString(userCursor.getColumnIndex("password")),
                userCursor.getInt(userCursor.getColumnIndex("userGoal"))
            )
        }
        userCursor.close()
        return userModel
    }
*/


}