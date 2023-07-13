package com.example.onvote.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.onvote.datamodel.UserModel

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
            "CREATE TABLE USERS(sID INTEGER PRIMARY KEY AUTOINCREMENT, sName TEXT, sFaculty TEXT, sUsername TEXT UNIQUE, sEmail TEXT, sPass TEXT, sVote INTEGER DEFAULT 0, isAdmin INTEGER DEFAULT 0)"
        )
        myDB.execSQL(
            "CREATE TABLE CANDIDATES(cID INTEGER PRIMARY KEY AUTOINCREMENT, sID INTEGER DEFAULT 0, cManif TEXT DEFAULT 'None', cAchieve DEFAULT 'None', cApprove INTEGER DEFAULT 0, cVotes INTEGER DEFAULT 0)"
        )
    }

    override fun onUpgrade(myDB: SQLiteDatabase, i: Int, i1: Int) {
        myDB.execSQL("DROP TABLE IF EXISTS USERS")
        myDB.execSQL("DROP TABLE IF EXISTS CANDIDATES")
        onCreate(myDB)
    }

    //insert user
    fun insertData(sName: String, sFaculty: String, sUsername: String, sEmail: String, sPass: String): Boolean {
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sName", sName)
        contentValues.put("sFaculty", sFaculty)
        contentValues.put("sUsername", sUsername)
        contentValues.put("sEmail", sEmail)
        contentValues.put("sPass", sPass)
        val result = myDB.insert("USERS", null, contentValues)

        return result != -1L
    }


    //insert candidate
    fun insertCandidate(sID:Int, cAchieve: String, cManif:String): Boolean{
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sID", sID)
        contentValues.put("cAchieve", cAchieve)
        contentValues.put("cManif", cManif)
        val result = myDB.insert("CANDIDATES", null, contentValues)

        return result != -1L
    }

    //insert vote
    fun insertVote(cID: Int): Boolean{
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("cID", cID)
        val result = myDB.insert("CANDIDATES", null, contentValues)

        return result != -1L
    }

    //check username if exist
    fun checkUsername(sUsername: String): Boolean {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE sUsername = ?",
            arrayOf(sUsername)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //check user username & pass
    @SuppressLint("Range")
    fun checkUsernamePassword(sUsername: String, sPass: String): Int {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE sUsername = ? and sPass = ?",
            arrayOf(sUsername, sPass)
        )

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val sID = cursor.getInt(0)
            cursor.close()
            Log.d(TAG, "checkUsernamePassword: $sID")
            return sID
        } else {
            cursor.close()
            return -1
        }
    }

    //check if user is admin
    @SuppressLint("Range")
    fun checkAdmin(sUsername: String, sPass: String): Int {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE sUsername = ? and sPass = ? and isAdmin = 1",
            arrayOf(sUsername, sPass)
        )

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val sID = cursor.getInt(0)
            cursor.close()
            Log.d(TAG, "adminID: $sID")
            return sID
        } else {
            cursor.close()
            return -1
        }
    }

    //read user
    @SuppressLint("Range")
    fun getUser(sID: Int): UserModel? {
        val db = this.readableDatabase
        var userModel: UserModel? = null
        val query = "SELECT * FROM users WHERE sID = ?"
        val userCursor = db.rawQuery(query, arrayOf(sID.toString()))

        if (userCursor.moveToFirst()) {
            userModel = UserModel(
                userCursor.getInt(userCursor.getColumnIndex("sID")),
                userCursor.getString(userCursor.getColumnIndex("sName")),
                userCursor.getString(userCursor.getColumnIndex("sFaculty")),
                userCursor.getString(userCursor.getColumnIndex("sUsername")),
                userCursor.getString(userCursor.getColumnIndex("sEmail")),
                userCursor.getString(userCursor.getColumnIndex("sPass")),
                userCursor.getInt(userCursor.getColumnIndex("sVote")),
                userCursor.getInt(userCursor.getColumnIndex("isAdmin"))
            )
        }
        userCursor.close()
        return userModel
    }


}